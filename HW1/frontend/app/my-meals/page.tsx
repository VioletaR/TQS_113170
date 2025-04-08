"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "../contexts/AuthContext";
import { userMealService } from "@/lib/services/user-meal.service";
import { UserMealDTO } from "@/lib/services/api";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { getWeatherIcon, getWeatherDescription } from "@/lib/utils/weather";
import { getMealType } from "../../lib/utils/meal";
import Image from "next/image";
import { ChevronDown, ChevronUp } from "lucide-react";
import { Building2 } from "lucide-react";

export default function MyMealsPage() {
  const router = useRouter();
  const { isAuthenticated, user } = useAuth();
  const [meals, setMeals] = useState<UserMealDTO[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [groupedMeals, setGroupedMeals] = useState<Record<string, UserMealDTO[]>>({});
  const [expandedDates, setExpandedDates] = useState<Record<string, boolean>>({});

  useEffect(() => {
    if (!isAuthenticated) {
      router.push("/");
      return;
    }
    loadMyMeals();
  }, [isAuthenticated, router]);

  useEffect(() => {
    if (meals.length > 0) {
      const grouped = meals.reduce((acc, meal) => {
        const date = new Date(meal.userMeal.meal.date);
        const dateKey = date.toISOString().split('T')[0];
        if (!acc[dateKey]) {
          acc[dateKey] = [];
        }
        acc[dateKey].push(meal);
        return acc;
      }, {} as Record<string, UserMealDTO[]>);
      setGroupedMeals(grouped);

      // Initialize all dates as expanded
      const initialExpanded = Object.keys(grouped).reduce((acc, date) => {
        acc[date] = true;
        return acc;
      }, {} as Record<string, boolean>);
      setExpandedDates(initialExpanded);
    }
  }, [meals]);

  const loadMyMeals = async () => {
    if (!user?.id) return;
    try {
      const myMeals = await userMealService.getAllByUserId(user.id);
      console.log("Meals before loading:", myMeals);
      setMeals(myMeals);
      
      console.log("Meals after loading:", myMeals);
    } catch (error) {
      console.error("Failed to load meals:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancelReservation = async (userMealId: number) => {
    if (!confirm("Are you sure you want to cancel this reservation?")) {
      return;
    }

    try {
      await userMealService.delete(userMealId);
      // Update the state immediately after successful deletion
      setMeals(prevMeals => prevMeals.filter(meal => meal.userMeal.id !== userMealId));
      // Also update the grouped meals
      setGroupedMeals(prev => {
        const newGrouped = { ...prev };
        Object.keys(newGrouped).forEach(date => {
          newGrouped[date] = newGrouped[date].filter(meal => meal.userMeal.id !== userMealId);
          if (newGrouped[date].length === 0) {
            delete newGrouped[date];
          }
        });
        return newGrouped;
      });
    } catch (error) {
      console.error("Failed to cancel reservation:", error);
      alert("Failed to cancel reservation. Please try again.");
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const toggleDate = (date: string) => {
    setExpandedDates(prev => ({
      ...prev,
      [date]: !prev[date]
    }));
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-xl" data-testid="loading-message">Loading...</div>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-4 space-y-8" data-testid="my-meals-page">
      <h1 className="text-3xl font-bold" data-testid="page-title">My Meals</h1>
      
      {Object.entries(groupedMeals).map(([date, dateMeals]) => (
        <div key={date} className="space-y-4">
          <Button
            variant="ghost"
            className="w-full flex justify-between items-center p-0 hover:bg-transparent"
            onClick={() => toggleDate(date)}
            data-testid={`date-header-${date}`}
          >
            <h2 className="text-2xl font-semibold">{formatDate(date)}</h2>
            {expandedDates[date] ? (
              <ChevronUp className="h-6 w-6" data-testid={`chevron-up-${date}`} />
            ) : (
              <ChevronDown className="h-6 w-6" data-testid={`chevron-down-${date}`} />
            )}
          </Button>
          
          {expandedDates[date] && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {dateMeals.map((meal) => (
                <Card key={meal.userMeal.id} className="w-full overflow-hidden transition-all duration-300 hover:shadow-lg" data-testid={`meal-card-${meal.userMeal.id}`}>
                  <CardHeader className="pb-2">
                    <div className="flex justify-between items-start">
                      <CardTitle className="text-xl font-bold" data-testid="meal-name">{meal.userMeal.meal.name}</CardTitle>
                      <Badge variant="outline" className="text-sm font-medium" data-testid="meal-price">€{meal.userMeal.meal.price}</Badge>
                    </div>
                    <div className="flex items-center gap-2 mt-2">
                      <Badge variant="secondary" className="text-xs" data-testid="meal-time">
                        {new Date(meal.userMeal.meal.date).toLocaleTimeString('en-US', {
                          hour: '2-digit',
                          minute: '2-digit',
                          hour12: true
                        })}
                      </Badge>
                      <Badge variant="default" className="text-xs" data-testid="meal-type">
                        {getMealType(new Date(meal.userMeal.meal.date))}
                      </Badge>
                    </div>
                  </CardHeader>
                  <CardContent className="space-y-4">
                    <div className="flex items-center gap-2">
                      <Building2 className="h-4 w-4 text-muted-foreground" />
                      <div className="flex flex-col">
                        <span className="text-sm font-medium" data-testid="restaurant-name">{meal.userMeal.meal.restaurant.name}</span>
                        <span className="text-xs text-muted-foreground" data-testid="restaurant-location">{meal.userMeal.meal.restaurant.location}</span>
                      </div>
                    </div>

                    {meal.weatherIPMA && (
                      <div className="flex items-center gap-3 p-3 bg-muted/50 rounded-lg">
                        <div className="relative w-24 h-24">
                          <Image
                            src={getWeatherIcon(meal.weatherIPMA.typeOfWeatherId, new Date(meal.userMeal.meal.date), true)}
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

                    <div className="flex items-center gap-2">
                      <div className="p-3 bg-primary/10 rounded-lg flex-1">
                        <div className="text-sm font-medium text-primary" data-testid="reservation-code">
                          Reservation Code: {meal.userMeal.code}
                        </div>
                      </div>
                      <Badge variant={meal.userMeal.isCheck ? "destructive" : "default"} className="text-xs" data-testid="check-status">
                        {meal.userMeal.isCheck ? "Checked In" : "Not Checked In"}
                      </Badge>
                    </div>

                    {!meal.userMeal.isCheck && (
                      <Button
                        variant="destructive"
                        className="w-full"
                        onClick={() => handleCancelReservation(meal.userMeal.id!)}
                        data-testid="cancel-button"
                      >
                        Cancel Reservation
                      </Button>
                    )}
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>
      ))}

      {Object.keys(groupedMeals).length === 0 && (
        <div className="text-center text-gray-500" data-testid="no-meals-message">
          No meals booked yet.
        </div>
      )}
    </div>
  );
} 