package testit

import java.io.ByteArrayOutputStream

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