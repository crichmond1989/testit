package testit

import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class CpsPrintStream extends PrintStream implements Serializable {
    CpsPrintStream() {
    }

    CpsPrintStream(ByteArrayOutputStream stream) {
        super(stream)
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}