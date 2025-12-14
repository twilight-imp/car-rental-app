package grpc.pricing;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;

@GrpcService
public class PricingServiceImpl extends PricingServiceGrpc.PricingServiceImplBase {

    @Override
    public void calculateTotalPrice(CalculatePriceRequest request, StreamObserver<CalculatePriceResponse> responseObserver){
        double baseTotal = request.getCarBasePrice() * request.getRentalDays();

        double ageSurcharge = calculateAgeSurcharge(request.getCustomerAge(), baseTotal);
        double experienceSurcharge = calculateExperienceSurcharge(request.getDrivingExperience(), baseTotal);
        double timeSurcharge = calculateTimeSurcharge(determineTimeOfDay(), baseTotal);

        double totalSurcharges = ageSurcharge + experienceSurcharge + timeSurcharge;
        double finalPrice = Math.round((baseTotal + totalSurcharges) * 100.0) / 100.0;


        CalculatePriceResponse response = CalculatePriceResponse.newBuilder()
                .setBaseTotal(baseTotal)
                .setFinalPrice(finalPrice)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    private double calculateAgeSurcharge(int age, double baseTotal) {
        if (age < 21) return baseTotal * 0.5;
        if (age < 25) return baseTotal * 0.3;
        if (age < 60) return 0.0;
        return baseTotal * 0.2;
    }

    private double calculateExperienceSurcharge(int experience, double baseTotal) {
        if (experience < 1) return baseTotal * 0.8;
        if (experience < 3) return baseTotal * 0.6;
        if (experience < 5) return baseTotal * 0.4;
        if (experience < 10) return 0.2;
        return 0.0;
    }

    private String determineTimeOfDay() {
        int hour = LocalDateTime.now().getHour();
        if (hour >= 6 && hour < 12) return "morning";
        if (hour >= 12 && hour < 18) return "afternoon";
        if (hour >= 18 && hour < 23) return "evening";
        return "night";
    }

    private double calculateTimeSurcharge(String timeOfDay, double baseTotal) {
        if (timeOfDay.toLowerCase().equals("night")) return baseTotal * 0.4;
        if (timeOfDay.toLowerCase().equals("evening")) return baseTotal * 0.2;
        return 0.0;
    }
}