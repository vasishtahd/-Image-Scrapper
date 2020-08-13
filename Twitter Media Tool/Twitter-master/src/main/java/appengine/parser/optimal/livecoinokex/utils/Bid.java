package appengine.parser.optimal.livecoinokex.utils;

public  class Bid {
    public Double price;
    public Double amount;

    public Bid(Double price, Double amount) {
        this.price = price;
        this.amount = amount;
    }
}