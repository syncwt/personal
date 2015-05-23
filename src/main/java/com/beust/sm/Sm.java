package com.beust.sm;

public class Sm {
    public static void main(String[] args) {
        Sm a = new Sm();
        a.run();
    }

    enum StateType {
        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }

    public class Machine {

        private StateType current;

        public Machine(StateType initial) {
            this.current = initial;
        }
        
    }

    private void run() {
        Machine m = new Machine(StateType.DISCONNECTED);
    }
}
