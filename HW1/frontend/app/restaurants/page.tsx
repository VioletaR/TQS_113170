"use client";

import { useState, useEffect } from "react";
import { RestaurantSearch } from "../components/RestaurantSearch";
import { RestaurantCard } from "../components/RestaurantCard";
import { restaurantService } from "@/lib/services/restaurant.service";
import { Restaurant } from "@/lib/services/api";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ChevronDown, ChevronUp } from "lucide-react";
import { Button } from "@/components/ui/button";

export default function RestaurantsPage() {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [filteredRestaurants, setFilteredRestaurants] = useState<Restaurant[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [groupedRestaurants, setGroupedRestaurants] = useState<Record<string, Restaurant[]>>({});
  const [expandedLocations, setExpandedLocations] = useState<Record<string, boolean>>({});

  useEffect(() => {
    loadRestaurants();
  }, []);

  useEffect(() => {
    if (filteredRestaurants.length > 0) {
      const grouped = filteredRestaurants.reduce((acc, restaurant) => {
        const location = restaurant.location;
        if (!acc[location]) {
          acc[location] = [];
        }
        acc[location].push(restaurant);
        return acc;
      }, {} as Record<string, Restaurant[]>);
      setGroupedRestaurants(grouped);
      
      // Initialize all locations as expanded
      const initialExpanded = Object.keys(grouped).reduce((acc, location) => {
        acc[location] = true;
        return acc;
      }, {} as Record<string, boolean>);
      setExpandedLocations(initialExpanded);
    }
  }, [filteredRestaurants]);

  const loadRestaurants = async () => {
    try {
      const data = await restaurantService.getAll();
      setRestaurants(data);
      setFilteredRestaurants(data);
    } catch (error) {
      console.error("Failed to load restaurants:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSearch = (query: string) => {
    const filtered = restaurants.filter(
      (restaurant) =>
        restaurant.name.toLowerCase().includes(query.toLowerCase()) ||
        restaurant.location.toLowerCase().includes(query.toLowerCase())
    );
    setFilteredRestaurants(filtered);
  };

  const toggleLocation = (location: string) => {
    setExpandedLocations(prev => ({
      ...prev,
      [location]: !prev[location]
    }));
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-xl">Loading restaurants...</div>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-4 space-y-6">
      <h1 className="text-3xl font-bold">Restaurants</h1>
      <RestaurantSearch onSearch={handleSearch} />
      
      <div className="space-y-8">
        {Object.entries(groupedRestaurants).map(([location, locationRestaurants]) => (
          <div key={location} className="space-y-4">
            <Button
              variant="ghost"
              className="w-full flex justify-between items-center p-0 hover:bg-transparent"
              onClick={() => toggleLocation(location)}
            >
              <h2 className="text-2xl font-semibold">{location}</h2>
              {expandedLocations[location] ? (
                <ChevronUp className="h-6 w-6" />
              ) : (
                <ChevronDown className="h-6 w-6" />
              )}
            </Button>
            {expandedLocations[location] && (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {locationRestaurants.map((restaurant) => (
                  <RestaurantCard key={restaurant.id} restaurant={restaurant} />
                ))}
              </div>
            )}
          </div>
        ))}

        {Object.keys(groupedRestaurants).length === 0 && (
          <div className="text-center text-gray-500">
            No restaurants found matching your search.
          </div>
        )}
      </div>
    </div>
  );
} 