package com.unir.ata;


public class FFT {

    private static final int MAX_SIZE = 1 << 15;
    // Private class data
    private final int numberOfBits;
    private final int [] invertedBits = new int[MAX_SIZE];
    private static final double PI_TWO =  Math.PI * 2.0;
    public FFT(int numberOfBits) {

        this.numberOfBits = numberOfBits;

        for (int i = (1 << numberOfBits) - 1; i >= 0; --i) {
            int k = 0;
            for (int j = 0; j < numberOfBits; ++j) {
                k *= 2;
                if ((i & (1 << j)) != 0)
                    k++;
            }
            this.invertedBits[i] = k;
        }
    }


    public void doFFT(double [] realPart, double [] imaginaryPart, boolean inverseTransform) {
        int n, n2;
        int i, j, k;
        int jn2, invertedjn2;
        double alfa, sin, cos, tReal, tImaginary;

        n2 = (n = (1 << this.numberOfBits)) / 2;

        for (k = 0; k < this.numberOfBits; ++k) {
            for (j = 0; j < n; j += n2) {
                for (i = 0; i < n2; ++i, ++j) {
                    invertedjn2 = this.invertedBits[j / n2];
                    alfa = PI_TWO * invertedjn2 / n;
                    cos = Math.cos(alfa);
                    sin = Math.sin(alfa);
                    jn2 = j + n2;

                    if (inverseTransform) {
                        sin = -sin;
                    }
                    tReal = realPart[jn2] * cos + imaginaryPart[jn2] * sin;
                    tImaginary = imaginaryPart[jn2] * cos - realPart[jn2] * sin;

                    realPart[jn2] = realPart[j] - tReal;
                    imaginaryPart[jn2] = imaginaryPart[j] - tImaginary;
                    realPart[j] += tReal;
                    imaginaryPart[j] += tImaginary;
                }
            }
            n2 /= 2;
        }

        for (j = 0; j < n; j++) {
            if ((i = this.invertedBits[j]) <= j)
                continue;

            tReal = realPart[j];
            tImaginary = imaginaryPart[j];
            realPart[j] = realPart[i];
            imaginaryPart[j] = imaginaryPart[i];
            realPart[i] = tReal;
            imaginaryPart[i] = tImaginary;
        }

        if (!inverseTransform) {
            double d = 1.0 / n;

            for (i = 0; i < n ; i++) {
                realPart[i] *= d;
                imaginaryPart[i] *= d;
            }
        }
    }

}

