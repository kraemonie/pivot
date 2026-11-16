"""Train Pivot's small offline handwritten-digit reader from the official MNIST data.

Requires numpy. Download mnist.npz from the Keras dataset URL into artifacts/scan-upgrade.
Weights and metrics are reproducible with a fixed seed. Test data never selects weights.
"""
import os
os.environ.setdefault('OPENBLAS_NUM_THREADS', '2')
import json
import struct
from pathlib import Path
import numpy as np

rng = np.random.default_rng(741)
data = np.load('artifacts/scan-upgrade/mnist.npz', allow_pickle=False)
x = data['x_train'].reshape(-1, 784).astype(np.float32) / 255
y = data['y_train'].astype(int)
validation_x, validation_y = x[-5000:], y[-5000:]
x, y = x[:-5000], y[:-5000]
sizes = [784, 128, 64, 10]
parameters = []
for a, b in zip(sizes, sizes[1:]):
    parameters.extend([(rng.standard_normal((a,b)) * np.sqrt(2/a)).astype(np.float32), np.zeros(b, np.float32)])
moments = [np.zeros_like(p) for p in parameters]
velocities = [np.zeros_like(p) for p in parameters]

def forward(batch, weights):
    h1 = np.maximum(0, batch @ weights[0] + weights[1])
    h2 = np.maximum(0, h1 @ weights[2] + weights[3])
    return h1, h2, h2 @ weights[4] + weights[5]

def accuracy(samples, labels, weights):
    correct = 0
    for start in range(0, len(samples), 512):
        logits = forward(samples[start:start+512], weights)[2]
        correct += np.sum(logits.argmax(1) == labels[start:start+512])
    return float(correct / len(samples))

step = 0
best_accuracy = 0
best = None
for epoch in range(24):
    order = rng.permutation(len(x))
    rate = .0012 * (.3 + .7 * (1-epoch/24))
    for start in range(0, len(order), 256):
        indices = order[start:start+256]
        batch, labels = x[indices].copy(), y[indices]
        h1, h2, logits = forward(batch, parameters)
        logits -= logits.max(1, keepdims=True)
        probability = np.exp(logits)
        probability /= probability.sum(1, keepdims=True)
        probability[np.arange(len(labels)), labels] -= 1
        probability /= len(labels)
        d2 = (probability @ parameters[4].T) * (h2 > 0)
        d1 = (d2 @ parameters[2].T) * (h1 > 0)
        gradients = [batch.T @ d1, d1.sum(0), h1.T @ d2, d2.sum(0), h2.T @ probability, probability.sum(0)]
        step += 1
        for p, grad, m, v in zip(parameters, gradients, moments, velocities):
            m *= .9
            m += .1 * grad
            v *= .999
            v += .001 * grad * grad
            p -= rate * (m/(1-.9**step)) / (np.sqrt(v/(1-.999**step)) + 1e-8)
    score = accuracy(validation_x, validation_y, parameters)
    if score > best_accuracy:
        best_accuracy, best = score, [p.copy() for p in parameters]
    print(f'Epoch {epoch+1}/24: validation accuracy {score:.4%}', flush=True)

test_x = data['x_test'].reshape(-1, 784).astype(np.float32) / 255
test_accuracy = accuracy(test_x, data['y_test'], best)
assert test_accuracy >= .975, 'Digit model did not meet the minimum test benchmark'
target = Path('web/models')
target.mkdir(parents=True, exist_ok=True)
with (target/'digits.bin').open('wb') as model:
    model.write(struct.pack('>5i', 0x50564431, *sizes))
    for parameter in best:
        model.write(parameter.astype('>f4').tobytes())
metrics = {'dataset':'MNIST', 'seed':741, 'validation_count':5000, 'validation_accuracy':best_accuracy,
           'test_count':10000, 'test_accuracy':test_accuracy, 'layers':sizes,
           'scope':'Isolated handwritten digits; this is not photographed-matrix accuracy.'}
(target/'digits-metrics.json').write_text(json.dumps(metrics, indent=2)+'\n')
print(json.dumps(metrics), flush=True)
