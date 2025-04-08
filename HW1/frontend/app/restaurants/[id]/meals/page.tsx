"use client";

import { useState, useEffect } from "react";
import { useParams } from "next/navigation";
import { MealCard } from "@/app/components/MealCard";
import { mealService } from "@/lib/services/meal.service";
import { MealDTO } from "@/lib/services/api";
import { restaurantService } from "@/lib/services/restaurant.service";
import { Restaurant } from "@/lib/services/api";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { ChevronDown, ChevronUp } from "lucide-react";
import { useAuth } from "@/app/contexts/AuthContext";
import { userMealService } from "@/lib/services/user-meal.service";
import { UserMealDTO } from "@/lib/services/api";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";

export default function RestaurantMealsPage() {
  const params = useParams();
  const restaurantId = Number(params.id);
  const { user } = useAuth();
  const [meals, setMeals] = useState<MealDTO[]>([]);
  const [reservations, setReservations] = useState<UserMealDTO[]>([]);
  const [filteredReservations, setFilteredReservations] = useState<UserMealDTO[]>([]);
  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [groupedMeals, setGroupedMeals] = useState<Record<string, MealDTO[]>>({});
  const [groupedReservations, setGroupedReservations] = useState<Record<string, UserMealDTO[]>>({});
  const [expandedDates, setExpandedDates] = useState<Record<string, boolean>>({});
  const [searchQuery, setSearchQuery] = useState("");

  useEffect(() => {
    loadRestaurantAndData();
  }, [restaurantId]);

  useEffect(() => {
    if (meals.length > 0) {
      const grouped = meals.reduce((acc, meal) => {
        const date = new Date(meal.meal.date);
        const dateKey = date.toISOString().split('T')[0];
        if (!acc[dateKey]) {
          acc[dateKey] = [];
        }
        acc[dateKey].push(meal);
        return acc;
      }, {} as Record<string, MealDTO[]>);
      setGroupedMeals(grouped);

      // Initialize all dates as expanded
      const initialExpanded = Object.keys(grouped).reduce((acc, date) => {
        acc[date] = true;
        return acc;
      }, {} as Record<string, boolean>);
      setExpandedDates(initialExpanded);
    }
  }, [meals]);

  useEffect(() => {
    if (reservations.length > 0) {
      const filtered = searchQuery
        ? reservations.filter(reservation => 
            reservation.userMeal.meal.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
            reservation.userMeal.code.toLowerCase().includes(searchQuery.toLowerCase())
          )
        : reservations;
      setFilteredReservations(filtered);

      const grouped = filtered.reduce((acc, reservation) => {
        const date = new Date(reservation.userMeal.meal.date);
        const dateKey = date.toISOString().split('T')[0];
        if (!acc[dateKey]) {
          acc[dateKey] = [];
        }
        acc[dateKey].push(reservation);
        return acc;
      }, {} as Record<string, UserMealDTO[]>);
      setGroupedReservations(grouped);
    }
  }, [reservations, searchQuery]);

  const loadRestaurantAndData = async () => {
    try {
      const [restaurantData, mealsData] = await Promise.all([
        restaurantService.getById(restaurantId),
        mealService.getAllByRestaurantId(restaurantId)
      ]);
      setRestaurant(restaurantData);
      setMeals(mealsData);

      if (user?.role === "STAFF") {
        const reservationsData = await userMealService.getAllByRestaurantId(restaurantId);
        setReservations(reservationsData);
      }
    } catch (error) {
      console.error("Failed to load data:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCheckReservation = async (userMealId: number) => {
    try {
      const reservation = reservations.find(r => r.userMeal.id === userMealId);
      if (reservation) {
        await userMealService.update({
          ...reservation.userMeal,
          isCheck: !reservation.userMeal.isCheck
        });
        await loadRestaurantAndData();
      }
    } catch (error) {
      console.error("Failed to update reservation:", error);
      alert("Failed to update reservation status. Please try again.");
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

  if (!restaurant) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-xl text-red-500" data-testid="error-message">Restaurant not found</div>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-4 space-y-6" data-testid="restaurant-meals-page">
      <div className="space-y-2">
        <h1 className="text-3xl font-bold" data-testid="restaurant-name">{restaurant.name}</h1>
        <div className="flex items-center space-x-4 text-gray-600">
          <span data-testid="restaurant-location">{restaurant.location}</span>
          <span>•</span>
          <span data-testid="restaurant-seats">{restaurant.seats} seats</span>
        </div>
      </div>

      {user?.role === "STAFF" ? (
        <div className="space-y-8" data-testid="staff-view">
          <div className="space-y-4">
            <h2 className="text-2xl font-semibold" data-testid="reservations-title">Reservations</h2>
            <Input
              type="text"
              placeholder="Search by meal name or reservation code..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full"
              data-testid="reservations-search"
            />
          </div>
          {Object.entries(groupedReservations).map(([date, dateReservations]) => (
            <div key={date} className="space-y-4" data-testid={`reservation-date-group-${date}`}>
              <Button
                variant="ghost"
                className="w-full flex justify-between items-center p-0 hover:bg-transparent"
                onClick={() => toggleDate(date)}
                data-testid={`toggle-date-${date}`}
              >
                <h3 className="text-xl font-semibold" data-testid={`date-header-${date}`}>{formatDate(date)}</h3>
                {expandedDates[date] ? (
                  <ChevronUp className="h-6 w-6" data-testid={`chevron-up-${date}`} />
                ) : (
                  <ChevronDown className="h-6 w-6" data-testid={`chevron-down-${date}`} />
                )}
              </Button>
              {expandedDates[date] && (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6" data-testid={`reservations-list-${date}`}>
                  {dateReservations.map((reservation) => (
                    <Card key={reservation.userMeal.id} data-testid={`reservation-card-${reservation.userMeal.id}`}>
                      <CardHeader>
                        <CardTitle data-testid={`meal-name-${reservation.userMeal.id}`}>{reservation.userMeal.meal.name}</CardTitle>
                      </CardHeader>
                      <CardContent className="space-y-4">
                        <div className="flex items-center space-x-2">
                          <Badge variant="outline">Time</Badge>
                          <span data-testid={`meal-time-${reservation.userMeal.id}`}>
                            {new Date(reservation.userMeal.meal.date).toLocaleTimeString('en-US', {
                              hour: '2-digit',
                              minute: '2-digit',
                              hour12: true
                            })}
                          </span>
                        </div>
                        <div className="flex items-center space-x-2">
                          <Badge variant="outline">Reservation Code</Badge>
                          <span className="font-mono" data-testid={`reservation-code-${reservation.userMeal.id}`}>
                            {reservation.userMeal.code}
                          </span>
                        </div>
                        <div className="flex items-center space-x-2">
                          <Badge 
                            variant={reservation.userMeal.isCheck ? "destructive" : "default"}
                            data-testid={`check-status-${reservation.userMeal.id}`}
                          >
                            {reservation.userMeal.isCheck ? "Checked In" : "Not Checked In"}
                          </Badge>
                        </div>
                        <Button
                          variant={reservation.userMeal.isCheck ? "outline" : "default"}
                          className="w-full"
                          onClick={() => handleCheckReservation(reservation.userMeal.id!)}
                          data-testid={`check-button-${reservation.userMeal.id}`}
                        >
                          {reservation.userMeal.isCheck ? "Mark as Not Checked In" : "Mark as Checked In"}
                        </Button>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              )}
            </div>
          ))}
          {Object.keys(groupedReservations).length === 0 && (
            <div className="text-center text-gray-500" data-testid="no-reservations-message">
              {searchQuery ? "No reservations found matching your search." : "No reservations found for this restaurant."}
            </div>
          )}
        </div>
      ) : (
        <div className="space-y-8" data-testid="user-view">
          {Object.entries(groupedMeals).map(([date, meals]) => (
            <div key={date} className="space-y-4" data-testid={`meal-date-group-${date}`}>
              <Button
                variant="ghost"
                className="w-full flex justify-between items-center p-0 hover:bg-transparent"
                onClick={() => toggleDate(date)}
                data-testid={`toggle-date-${date}`}
              >
                <h2 className="text-2xl font-semibold" data-testid={`date-header-${date}`}>{formatDate(date)}</h2>
                {expandedDates[date] ? (
                  <ChevronUp className="h-6 w-6" data-testid={`chevron-up-${date}`} />
                ) : (
                  <ChevronDown className="h-6 w-6" data-testid={`chevron-down-${date}`} />
                )}
              </Button>
              {expandedDates[date] && (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6" data-testid={`meals-list-${date}`}>
                  {meals.map((meal) => (
                    <MealCard key={meal.meal.id} meal={meal} />
                  ))}
                </div>
              )}
            </div>
          ))}

          {Object.keys(groupedMeals).length === 0 && (
            <div className="text-center text-gray-500" data-testid="no-meals-message">
              No meals available for this restaurant.
            </div>
          )}
        </div>
      )}
    </div>
  );
} 