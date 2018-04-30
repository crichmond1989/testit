package testit

import java.io.ByteArrayOutputStream

class CpsPrintStream extends PrintStream implements Serializable {
    CpsPrintStream(ByteArrayOutputStream stream) {
        super(stream)
    }
}