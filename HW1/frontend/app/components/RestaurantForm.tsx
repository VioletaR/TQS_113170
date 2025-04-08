import { Restaurant } from '@/lib/services/api';
import { FormEvent } from 'react';

interface RestaurantFormProps {
  restaurant?: Restaurant;
  onSubmit: (restaurant: Omit<Restaurant, 'id'>) => void;
}

export function RestaurantForm({ restaurant, onSubmit }: RestaurantFormProps) {
  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    
    const newRestaurant: Omit<Restaurant, 'id'> = {
      name: formData.get('name') as string,
      location: formData.get('location') as string,
      seats: parseInt(formData.get('seats') as string),
    };
    
    onSubmit(newRestaurant);
  };

  return (
    <form onSubmit={handleSubmit} className="max-w-md mx-auto">
      <div className="mb-4">
        <label htmlFor="name" className="block text-gray-700 font-medium mb-2">
          Name
        </label>
        <input
          type="text"
          id="name"
          name="name"
          defaultValue={restaurant?.name}
          required
          className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <div className="mb-4">
        <label htmlFor="location" className="block text-gray-700 font-medium mb-2">
          Location
        </label>
        <input
          type="text"
          id="location"
          name="location"
          defaultValue={restaurant?.location}
          required
          className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <div className="mb-6">
        <label htmlFor="seats" className="block text-gray-700 font-medium mb-2">
          Number of Seats
        </label>
        <input
          type="number"
          id="seats"
          name="seats"
          defaultValue={restaurant?.seats}
          required
          min="1"
          className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <button
        type="submit"
        className="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        {restaurant ? 'Update Restaurant' : 'Create Restaurant'}
      </button>
    </form>
  );
} 