package me.ifmo.stub;

public class TanStub extends AbstractTableStub {

    public TanStub() {
        put(-Math.PI / 4, -1.0);
        put(-Math.PI / 3, -Math.sqrt(3));
        put(-Math.PI / 6, -1.0 / Math.sqrt(3));
    }
}
