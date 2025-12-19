package model.economy;

import lombok.Getter;

@Getter
public class ConsumptionData {

    private double electricityUsed;
    private double oilUsed;
    private double wagesPaid;
    private double materialCost;

    public void consumeElectricity(double amount) {
        electricityUsed += amount;
    }

    public void consumeOil(double amount) {
        oilUsed += amount;
    }

    public void payWages(double amount) {
        wagesPaid += amount;
    }

    public ConsumptionSnapshot snapshot() {
        return new ConsumptionSnapshot(electricityUsed, oilUsed, wagesPaid,materialCost);
    }
}