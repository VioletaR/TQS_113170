"use client";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { useRouter } from "next/navigation";
import {  Users } from "lucide-react";

interface RestaurantCardProps {
  restaurant: {
    id: number;
    name: string;
    location: string;
    seats: number;
  };
}

export function RestaurantCard({ restaurant }: RestaurantCardProps) {
  const router = useRouter();

  return (
    <Card className="w-full overflow-hidden transition-all duration-300 hover:shadow-lg" data-testid={`restaurant-card-${restaurant.id}`}>
      <CardHeader className="pb-2">
        <div className="flex justify-between items-start">
          <CardTitle className="text-xl font-bold" data-testid="restaurant-name">{restaurant.name}</CardTitle>
          <Badge variant="outline" className="text-sm font-medium" data-testid="restaurant-location">
            {restaurant.location}
          </Badge>
        </div>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="flex items-center gap-2">
          <Users className="h-4 w-4 text-muted-foreground" />
          <div className="flex flex-col">
            <span className="text-sm font-medium">Available Seats</span>
            <span className="text-xs text-muted-foreground">
              {restaurant.seats} seats
            </span>
          </div>
        </div>

        <Button
          className="w-full"
          onClick={() => router.push(`/restaurants/${restaurant.id}/meals`)}
          data-testid="view-meals-button"
        >
          View Meals
        </Button>
      </CardContent>
    </Card>
  );
} 