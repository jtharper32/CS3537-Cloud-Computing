package src.main.java.edu.appstate.cs.cloud.blackjack;

public class Person {
    public static final String NAME = "name";
    public static final String BALANCE = "balance";

    private String name;
    private double balance;

    private Person(Builder builder) {
        this.name = builder.name;
        this.balance = builder.balance;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public static class Builder {
        private String name;
        private double balance;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withBalance(double balance) {
            this.balance = balance;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }
}