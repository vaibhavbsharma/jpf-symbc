/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.turkoid.imagetweaker;
package veritesting.benchmarksElena;
import java.io.*;
import java.awt.*;
import java.awt.image.*;

/**
 * Class AnimatedGifEncoder - Encodes a GIF file consisting of one or more
 * frames.
 *
 * <pre>
 *  Example:
 *     AnimatedGifEncoder e = new AnimatedGifEncoder();
 *     e.start(outputFileName);
 *     e.setDelay(1000);   // 1 frame per sec
 *     e.addFrame(image1);
 *     e.addFrame(image2);
 *     e.finish();
 * </pre>
 *
 * No copyright asserted on the source code of this class. May be used for any
 * purpose, however, refer to the Unisys LZW patent for restrictions on use of
 * the associated LZWEncoder class. Please forward any corrections to
 * kweiner@fmsware.com.
 *
 * @author Kevin Weiner, FM Software
 * @version 1.03 November 2003
 *
 */
public class AnimatedGifEncoder {

    protected int width; // image size
    protected int height;
    protected Color transparent = null; // transparent color if given
    protected int transIndex; // transparent index in color table
    protected int repeat = -1; // no repeat
    protected int delay = 0; // frame delay (hundredths)
    protected boolean started = false; // ready to output frames
    protected OutputStream out;
    protected BufferedImage image; // current frame
    protected byte[] pixels; // BGR byte array from frame
    protected byte[] indexedPixels; // converted frame indexed to palette
    protected int colorDepth; // number of bit planes
    protected byte[] colorTab; // RGB palette
    protected boolean[] usedEntry = new boolean[256]; // active palette entries
    protected int palSize = 7; // color table size (bits-1)
    protected int dispose = -1; // disposal code (-1 = use default)
    protected boolean closeStream = false; // close stream when finished
    protected boolean firstFrame = true;
    protected boolean sizeSet = false; // if false, get size from first frame
    protected int sample = 10; // default sample interval for quantizer
}

/*
 * NeuQuant Neural-Net Quantization Algorithm
 * ------------------------------------------
 *
 * Copyright (c) 1994 Anthony Dekker
 *
 * NEUQUANT Neural-Net quantization algorithm by Anthony Dekker, 1994. See
 * "Kohonen neural networks for optimal colour quantization" in "Network:
 * Computation in Neural Systems" Vol. 5 (1994) pp 351-367. for a discussion of
 * the algorithm.
 *
 * Any party obtaining a copy of these files from the author, directly or
 * indirectly, is granted, free of charge, a full and unrestricted irrevocable,
 * world-wide, paid up, royalty-free, nonexclusive right and license to deal in
 * this software and documentation files (the "Software"), including without
 * limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons who
 * receive copies from any such party to do so, with the only requirement being
 * that this copyright notice remain intact.
 */
// Ported to Java 12/00 K Weiner
class NeuQuant {

    protected static final int netsize = 256; /* number of colours used */

    /* four primes near 500 - assume no image has a length so large */
    /* that it is divisible by all four primes */
    protected static final int prime1 = 499;
    protected static final int prime2 = 491;
    protected static final int prime3 = 487;
    protected static final int prime4 = 503;
    protected static final int minpicturebytes = (3 * prime4);

    /* minimum size for input image */

    /*
     * Program Skeleton ---------------- [select samplefac in range 1..30] [read
     * image from input file] pic = (unsigned char*) malloc(3*width*height);
     * initnet(pic,3*width*height,samplefac); learn(); unbiasnet(); [write output
     * image header, using writecolourmap(f)] inxbuild(); write output image using
     * inxsearch(b,g,r)
     */

    /*
     * Network Definitions -------------------
     */
    protected static final int maxnetpos = (netsize - 1);
    protected static final int netbiasshift = 4; /* bias for colour values */

    protected static final int ncycles = 100; /* no. of learning cycles */

    /* defs for freq and bias */
    protected static final int intbiasshift = 16; /* bias for fractions */

    protected static final int intbias = (((int) 1) << intbiasshift);
    protected static final int gammashift = 10; /* gamma = 1024 */

    protected static final int gamma = (((int) 1) << gammashift);
    protected static final int betashift = 10;
    protected static final int beta = (intbias >> betashift); /* beta = 1/1024 */

    protected static final int betagamma = (intbias << (gammashift - betashift));

    /* defs for decreasing radius factor */
    protected static final int initrad = (netsize >> 3); /*
     * for 256 cols, radius
     * starts
     */

    protected static final int radiusbiasshift = 6; /* at 32.0 biased by 6 bits */

    protected static final int radiusbias = (((int) 1) << radiusbiasshift);
    protected static final int initradius = (initrad * radiusbias); /*
     * and
     * decreases
     * by a
     */

    protected static final int radiusdec = 30; /* factor of 1/30 each cycle */

    /* defs for decreasing alpha factor */
    protected static final int alphabiasshift = 10; /* alpha starts at 1.0 */

    protected static final int initalpha = (((int) 1) << alphabiasshift);
    protected int alphadec; /* biased by 10 bits */

    /* radbias and alpharadbias used for radpower calculation */
    protected static final int radbiasshift = 8;
    protected static final int radbias = (((int) 1) << radbiasshift);
    protected static final int alpharadbshift = (alphabiasshift + radbiasshift);
    protected static final int alpharadbias = (((int) 1) << alpharadbshift);

    /*
     * Types and Global Variables --------------------------
     */
    protected byte[] thepicture; /* the input image itself */

    protected int lengthcount; /* lengthcount = H*W*3 */

    protected int samplefac; /* sampling factor 1..30 */

    // typedef int pixel[4]; /* BGRc */
    protected int[][] network; /* the network itself - [netsize][4] */

    protected int[] netindex = new int[256];

    /* for network lookup - really 256 */
    protected int[] bias = new int[netsize];

    /* bias and freq arrays for learning */
    protected int[] freq = new int[netsize];
    protected int[] radpower = new int[initrad];

    /* radpower for precomputation */

    /*
     * Search for BGR values 0..255 (after net is unbiased) and return colour
     * index
     * ----------------------------------------------------------------------------
     */
    public int map(int b, int g, int r) {

        int i, j, dist, a, bestd;
        int[] p;
        int best;

        bestd = 1000; /* biggest possible dist is 256*3 */
        best = -1;
        i = netindex[g]; /* index on g */
        j = i - 1; /* start at netindex[g] and work outwards */

        while ((i < netsize) || (j >= 0)) {
            if (i < netsize) {
                p = network[i];
                dist = p[1] - g; /* inx key */
                if (dist >= bestd) {
                    i = netsize; /* stop iter */
                } else {
                    i++;
                    if (dist < 0) {
                        dist = -dist;
                    }
                    a = p[0] - b;
                    if (a < 0) {
                        a = -a;
                    }
                    dist += a;
                    if (dist < bestd) {
                        a = p[2] - r;
                        if (a < 0) {
                            a = -a;
                        }
                        dist += a;
                        if (dist < bestd) {
                            bestd = dist;
                            best = p[3];
                        }
                    }
                }
            }
            if (j >= 0) {
                p = network[j];
                dist = g - p[1]; /* inx key - reverse dif */
                if (dist >= bestd) {
                    j = -1; /* stop iter */
                } else {
                    j--;
                    if (dist < 0) {
                        dist = -dist;
                    }
                    a = p[0] - b;
                    if (a < 0) {
                        a = -a;
                    }
                    dist += a;
                    if (dist < bestd) {
                        a = p[2] - r;
                        if (a < 0) {
                            a = -a;
                        }
                        dist += a;
                        if (dist < bestd) {
                            bestd = dist;
                            best = p[3];
                        }
                    }
                }
            }
        }
        return (best);
    }

    /*
     * Move adjacent neurons by precomputed alpha*(1-((i-j)^2/[r]^2)) in
     * radpower[|i-j|]
     * ---------------------------------------------------------------------------------
     */
    protected void alterneigh(int rad, int i, int b, int g, int r) {

        int j, k, lo, hi, a, m;
        int[] p;

        lo = i - rad;
        if (lo < -1) {
            lo = -1;
        }
        hi = i + rad;
        if (hi > netsize) {
            hi = netsize;
        }

        j = i + 1;
        k = i - 1;
        m = 1;
        while ((j < hi) || (k > lo)) {
            a = radpower[m++];
            if (j < hi) {
                p = network[j++];
                try {
                    p[0] -= (a * (p[0] - b)) / alpharadbias;
                    p[1] -= (a * (p[1] - g)) / alpharadbias;
                    p[2] -= (a * (p[2] - r)) / alpharadbias;
                } catch (Exception e) {
                } // prevents 1.3 miscompilation
            }
            if (k > lo) {
                p = network[k--];
                try {
                    p[0] -= (a * (p[0] - b)) / alpharadbias;
                    p[1] -= (a * (p[1] - g)) / alpharadbias;
                    p[2] -= (a * (p[2] - r)) / alpharadbias;
                } catch (Exception e) {
                }
            }
        }
    }

    /*
     * Search for biased BGR values ----------------------------
     */
    protected int contest(int b, int g, int r) {

        /* finds closest neuron (min dist) and updates freq */
        /* finds best neuron (min dist-bias) and returns position */
        /* for frequently chosen neurons, freq[i] is high and bias[i] is negative */
        /* bias[i] = gamma*((1/netsize)-freq[i]) */

        int i, dist, a, biasdist, betafreq;
        int bestpos, bestbiaspos, bestd, bestbiasd;
        int[] n;

        bestd = ~(((int) 1) << 31);
        bestbiasd = bestd;
        bestpos = -1;
        bestbiaspos = bestpos;

        for (i = 0; i < netsize; i++) {
            n = network[i];
            dist = n[0] - b;
            if (dist < 0) {
                dist = -dist;
            }
            a = n[1] - g;
            if (a < 0) {
                a = -a;
            }
            dist += a;
            a = n[2] - r;
            if (a < 0) {
                a = -a;
            }
            dist += a;
            if (dist < bestd) {
                bestd = dist;
                bestpos = i;
            }
            biasdist = dist - ((bias[i]) >> (intbiasshift - netbiasshift));
            if (biasdist < bestbiasd) {
                bestbiasd = biasdist;
                bestbiaspos = i;
            }
            betafreq = (freq[i] >> betashift);
            freq[i] -= betafreq;
            bias[i] += (betafreq << gammashift);
        }
        freq[bestpos] += beta;
        bias[bestpos] -= betagamma;
        return (bestbiaspos);
    }
}

// ==============================================================================
// Adapted from Jef Poskanzer's Java port by way of J. M. G. Elliott.
// K Weiner 12/00
class LZWEncoder {

    private static final int EOF = -1;
    private int imgW, imgH;
    private byte[] pixAry;
    private int initCodeSize;
    private int remaining;
    private int curPixel;
    // GIFCOMPR.C - GIF Image compression routines
    //
    // Lempel-Ziv compression based on 'compress'. GIF modifications by
    // David Rowley (mgardi@watdcsu.waterloo.edu)
    // General DEFINEs
    static final int BITS = 12;
    static final int HSIZE = 5003; // 80% occupancy
    // GIF Image compression - modified 'compress'
    //
    // Based on: compress.c - File compression ala IEEE Computer, June 1984.
    //
    // By Authors: Spencer W. Thomas (decvax!harpo!utah-cs!utah-gr!thomas)
    // Jim McKie (decvax!mcvax!jim)
    // Steve Davies (decvax!vax135!petsd!peora!srd)
    // Ken Turkowski (decvax!decwrl!turtlevax!ken)
    // James A. Woods (decvax!ihnp4!ames!jaw)
    // Joe Orost (decvax!vax135!petsd!joe)
    int n_bits; // number of bits/code
    int maxbits = BITS; // user settable max # bits/code
    int maxcode; // maximum code, given n_bits
    int maxmaxcode = 1 << BITS; // should NEVER generate this code
    int[] htab = new int[HSIZE];
    int[] codetab = new int[HSIZE];
    int hsize = HSIZE; // for dynamic table sizing
    int free_ent = 0; // first unused entry
    // block compression parameters -- after all codes are used up,
    // and compression rate changes, start over.
    boolean clear_flg = false;
    // Algorithm: use open addressing double hashing (no chaining) on the
    // prefix code / next character combination. We do a variant of Knuth's
    // algorithm D (vol. 3, sec. 6.4) along with G. Knott's relatively-prime
    // secondary probe. Here, the modular division first probe is gives way
    // to a faster exclusive-or manipulation. Also do block compression with
    // an adaptive reset, whereby the code table is cleared when the compression
    // ratio decreases, but after the table fills. The variable-length output
    // codes are re-sized at this point, and a special CLEAR code is generated
    // for the decompressor. Late addition: construct the table according to
    // file size for noticeable speed improvement on small files. Please direct
    // questions about this implementation to ames!jaw.
    int g_init_bits;
    int ClearCode;
    int EOFCode;
    // output
    //
    // Output the given code.
    // Inputs:
    // code: A n_bits-bit integer. If == -1, then EOF. This assumes
    // that n_bits =< wordsize - 1.
    // Outputs:
    // Outputs code to the file.
    // Assumptions:
    // Chars are 8 bits long.
    // Algorithm:
    // Maintain a BITS character long buffer (so that 8 codes will
    // fit in it exactly). Use the VAX insv instruction to insert each
    // code in turn. When the buffer fills up empty it and start over.
    int cur_accum = 0;
    int cur_bits = 0;
    int masks[] = {0x0000, 0x0001, 0x0003, 0x0007, 0x000F, 0x001F, 0x003F, 0x007F, 0x00FF, 0x01FF,
        0x03FF, 0x07FF, 0x0FFF, 0x1FFF, 0x3FFF, 0x7FFF, 0xFFFF};
    // Number of characters so far in this 'packet'
    int a_count;
    // Define the storage for the packet accumulator
    byte[] accum = new byte[256];
}
