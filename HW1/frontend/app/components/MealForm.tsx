import { Meal } from '@/lib/services/api';
import { FormEvent } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface MealFormProps {
  meal?: Meal;
  onSubmit: (meal: Omit<Meal, 'id'>) => void;
}

export function MealForm({ meal, onSubmit }: MealFormProps) {
  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    
    const newMeal: Omit<Meal, 'id'> = {
      name: formData.get('name') as string,
      price: parseFloat(formData.get('price') as string),
      date: formData.get('date') as string,
      restaurant: meal?.restaurant || {
        id: parseInt(formData.get('restaurantId') as string),
        name: '',
        location: '',
        seats: 0,
      },
    };
    
    onSubmit(newMeal);
  };

  return (
    <Card className="max-w-md mx-auto">
      <CardHeader>
        <CardTitle>{meal ? 'Edit Meal' : 'Create New Meal'}</CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="name">Meal Name</Label>
            <Input
              type="text"
              id="name"
              name="name"
              defaultValue={meal?.name}
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="price">Price (€)</Label>
            <Input
              type="number"
              id="price"
              name="price"
              defaultValue={meal?.price}
              required
              min="0"
              step="0.01"
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="date">Date</Label>
            <Input
              type="datetime-local"
              id="date"
              name="date"
              defaultValue={meal?.date ? new Date(meal.date).toISOString().slice(0, 16) : ''}
              required
            />
          </div>

          {!meal && (
            <div className="space-y-2">
              <Label htmlFor="restaurantId">Restaurant ID</Label>
              <Input
                type="number"
                id="restaurantId"
                name="restaurantId"
                required
              />
            </div>
          )}

          <Button type="submit" className="w-full">
            {meal ? 'Update Meal' : 'Create Meal'}
          </Button>
        </form>
      </CardContent>
    </Card>
  );
} 