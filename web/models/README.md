# Offline handwriting model

`digits.bin` is Pivot's 784 → 128 → 64 → 10 dense ReLU network for isolated handwritten digits. Java inference uses only the bundled weights; no network request or extra inference runtime is required.

Reproduce with `python scripts/train-digits.py` and NumPy. Download `mnist.npz` from the [official Keras dataset mirror](https://storage.googleapis.com/tensorflow/tf-keras-datasets/mnist.npz) into `artifacts/scan-upgrade/` first. Training uses 55,000 images; 5,000 training images select weights. The separate 10,000-image test set is evaluated once after selection.

The recorded test accuracy is 98.07% on isolated MNIST digits. This is **not** accuracy on phone photographs or entire matrices. Joined digits, unusual handwriting and fractions may need correction. Low-confidence model readings are marked `?`, and the user always reviews the result against the cropped photograph.

MNIST is by Yann LeCun and Corinna Cortes, derived from NIST handwriting data. [Keras identifies its license as CC BY-SA 3.0](https://keras.io/api/datasets/mnist/). The derived model weights and test-image fixtures are distributed under [CC BY-SA 3.0](https://creativecommons.org/licenses/by-sa/3.0/). Image preprocessing and network training are modifications made for Pivot.
