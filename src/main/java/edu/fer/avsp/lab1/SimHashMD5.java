package edu.fer.avsp.lab1;

import java.util.BitSet;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class SimHashMD5 {
  private static final int MD5_SIZE = 128;
  private final String text;

  public SimHashMD5(String text) {
    this.text = text;
  }

  public String toHexString() {
    return Hex.encodeHexString(toBitSet().toByteArray());
  }

  public BitSet toBitSet() {
    var words = text.split("\\s+");
    var sh = new int[MD5_SIZE];

    for (String word : words) {
      BitSet md5 = BitSet.valueOf(DigestUtils.md5(word));

      for (int i = 0; i < MD5_SIZE; i++) {
        if (md5.get(i)) {
          sh[i] += 1;
        } else {
          sh[i] -= 1;
        }
      }
    }

    BitSet hash = new BitSet(MD5_SIZE);
    for (int i = 0; i < MD5_SIZE; i++) {
      if (sh[i] >= 0) {
        hash.set(i);
      }
    }

    return hash;
  }
}
