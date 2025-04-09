"use client";

import { useState, useEffect } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { MealDTO } from "@/lib/services/api";
import { userMealService } from "@/lib/services/user-meal.service";
import { useRouter } from "next/navigation";
import { useAuth } from "../contexts/AuthContext";
import { getWeatherIcon, getWeatherDescription } from "@/lib/utils/weather";
import { getMealType } from "@/lib/utils/meal";
import Image from "next/image";
import { Building2 } from "lucide-react";
import { useToast } from "@/components/ui/use-toast";

interface MealCardProps {
  meal: MealDTO;
}

export function MealCard({ meal }: MealCardProps) {
  const router = useRouter();
  const { isAuthenticated, user } = useAuth();
  const { toast } = useToast();
  const [isLoading, setIsLoading] = useState(false);
  const [bookingSuccess, setBookingSuccess] = useState<boolean>(false);
  const [reservationCode, setReservationCode] = useState<string>("");
  const [isAlreadyBooked, setIsAlreadyBooked] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const checkExistingBooking = async () => {
      if (!isAuthenticated || !user?.id) return;

      try {
        const myMeals = await userMealService.getAllByUserId(user.id);
        const hasBooked = myMeals.some(
          (userMeal) => userMeal.userMeal.meal.id === meal.meal.id
        );
        setIsAlreadyBooked(hasBooked);
      } catch (error) {
        console.error("Failed to check existing bookings:", error);
        toast({
          variant: "destructive",
          title: "Error",
          description: "Failed to check your existing bookings. Please try again.",
        });
      }
    };

    checkExistingBooking();
  }, [isAuthenticated, user, meal.meal.id, toast]);

  const handleBooking = async () => {
    if (!isAuthenticated || !user) {
      router.push("/login");
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      const response = await userMealService.create({
        user: user,
        meal: meal.meal,
        isCheck: false,
        code: ""
      });
      setReservationCode(response.code);
      setBookingSuccess(true);
      setIsAlreadyBooked(true);
      toast({
        title: "Success",
        description: "Your reservation has been confirmed!",
      });
    } catch (error) {
      console.error("Unable to book the meal:", error);
      setError("Unable to book the meal.");
      toast({
        variant: "destructive",
        title: "Error",
        description: "Unable to book the meal.",
      });
    } finally {
      setIsLoading(false);
    }
  };


  return (
    <Card className="w-full overflow-hidden transition-all duration-300 hover:shadow-lg border-primary/20" data-testid={`meal-card-${meal.meal.id}`}>
      <CardHeader className="pb-2">
        <div className="flex justify-between items-start">
          <CardTitle className="text-xl font-bold" data-testid="meal-name">{meal.meal.name}</CardTitle>
          <Badge variant="outline" className="text-sm font-medium" data-testid="meal-price">€{meal.meal.price}</Badge>
        </div>
        <div className="flex items-center gap-2 mt-2">
          <Badge variant="secondary" className="text-xs" data-testid="meal-date">
            {new Date(meal.meal.date).toLocaleDateString('en-US', {
              weekday: 'short',
              month: 'short',
              day: 'numeric'
            })}
          </Badge>
          <Badge variant="secondary" className="text-xs" data-testid="meal-time">
            {new Date(meal.meal.date).toLocaleTimeString('en-US', {
              hour: '2-digit',
              minute: '2-digit',
              hour12: true
            })}
          </Badge>
          <Badge variant="default" className="text-xs" data-testid="meal-type">
            {getMealType(new Date(meal.meal.date))}
          </Badge>
        </div>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="flex items-center gap-2">
          <Building2 className="h-4 w-4 text-muted-foreground" />
          <div className="flex flex-col">
            <span className="text-sm font-medium" data-testid="restaurant-name">{meal.meal.restaurant.name}</span>
            <span className="text-xs text-muted-foreground" data-testid="restaurant-location">{meal.meal.restaurant.location}</span>
          </div>
        </div>

        {meal.weatherIPMA && (
          <div className="flex items-center gap-3 p-3 bg-muted/50 rounded-lg">
            <div className="relative w-24 h-24">
              <Image
                src={getWeatherIcon(meal.weatherIPMA.typeOfWeatherId, new Date(meal.meal.date), true)}
                alt={getWeatherDescription(meal.weatherIPMA.typeOfWeatherId)}
                fill
                className="object-contain"
                data-testid="weather-icon"
              />
            </div>
            <div className="flex-1" data-testid="weather-info">
              <div className="text-sm font-medium">{getWeatherDescription(meal.weatherIPMA.typeOfWeatherId)}</div>
              <div className="text-xs text-muted-foreground">
                {meal.weatherIPMA.tempMin}°C - {meal.weatherIPMA.tempMax}°C • 
                Wind: {meal.weatherIPMA.windDirection} • 
                Rain: {meal.weatherIPMA.precipitationProb}%
              </div>
            </div>
          </div>
        )}

        {error && (
          <div className="p-3 bg-destructive/10 rounded-lg text-destructive text-sm" data-testid="error-message">
            {error}
          </div>
        )}

        {bookingSuccess ? (
          <div className="space-y-3">
            <div className="p-3 bg-primary/10 rounded-lg">
              <div className="text-sm font-medium text-primary" data-testid="reservation-code">
                Reservation Code: {reservationCode}
              </div>
            </div>
            <div className="flex gap-2">
              <Button
                variant="outline"
                className="flex-1 border-primary/50 text-primary hover:bg-primary/10"
                onClick={() => router.push('/my-meals')}
                data-testid="view-bookings-button"
              >
                View My Bookings
              </Button>
              <Button
                variant="default"
                className="flex-1 bg-primary hover:bg-primary/90"
                onClick={() => {
                  setBookingSuccess(false);
                  setReservationCode("");
                }}
                data-testid="book-another-button"
              >
                Book Another
              </Button>
            </div>
          </div>
        ) : (
          <Button
            className="w-full bg-primary hover:bg-primary/90"
            onClick={handleBooking}
            disabled={isAlreadyBooked || isLoading}
            data-testid="book-button"
          >
            {isLoading ? "Booking..." : (isAlreadyBooked ? "Already Booked" : "Book Now")}
          </Button>
        )}
      </CardContent>
    </Card>
  );
} 