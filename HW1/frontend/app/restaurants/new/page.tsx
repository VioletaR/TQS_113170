import { RestaurantForm } from '@/app/components/RestaurantForm';
import { restaurantService } from '@/lib/services/restaurant.service';
import { redirect } from 'next/navigation';

export default function NewRestaurantPage() {
  async function createRestaurant(restaurant: Omit<Restaurant, 'id'>) {
    'use server';
    await restaurantService.create(restaurant);
    redirect('/restaurants');
  }

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Create New Restaurant</h1>
      <RestaurantForm onSubmit={createRestaurant} />
    </div>
  );
} 