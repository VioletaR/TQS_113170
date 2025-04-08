"use client";

import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface RestaurantSearchProps {
  onSearch: (query: string) => void;
}

export function RestaurantSearch({ onSearch }: RestaurantSearchProps) {
  return (
    <Card className="w-full">
      <CardHeader>
        <CardTitle>Search Restaurants</CardTitle>
      </CardHeader>
      <CardContent>
        <Input
          type="text"
          placeholder="Search by name or location..."
          onChange={(e) => onSearch(e.target.value)}
          className="w-full"
        />
      </CardContent>
    </Card>
  );
} 