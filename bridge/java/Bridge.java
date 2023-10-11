interface Computer {
    void print();
}

class Mac implements Computer {
    private final Printer printer;

    public Mac(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void print() {
        System.out.println("Print request for Mac");
        printer.printFile();
    }
}

class Linux implements Computer {
    private final Printer printer;

    public Linux(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void print() {
        System.out.println("Print request for Linux");
        printer.printFile();
    }
}

class Windows implements Computer {
    private final Printer printer;

    public Windows(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void print() {
        System.out.println("Print request for Windows");
        printer.printFile();
    }
}

// ============================================================

interface Printer {
    void printFile();
}

class Epson implements Printer {
    @Override
    public void printFile() {
        System.out.println("Printing by a EPSON Printer");
    }
}

class Hp implements Printer {
    @Override
    public void printFile() {
        System.out.println("Printing by a HP Printer");
    }
}