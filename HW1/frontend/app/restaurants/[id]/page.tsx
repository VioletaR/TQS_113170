import { restaurantService } from '@/lib/services/restaurant.service';
import Link from 'next/link';

interface RestaurantPageProps {
  params: {
    id: string;
  };
}

export default async function RestaurantPage({ params }: RestaurantPageProps) {
  const restaurant = await restaurantService.getById(parseInt(params.id));

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">{restaurant.name}</h1>
        <div className="space-x-2">
          <Link
            href={`/restaurants/${restaurant.id}/edit`}
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Edit
          </Link>
          <Link
            href="/restaurants"
            className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
          >
            Back to Restaurants
          </Link>
        </div>
      </div>

      <div className="bg-white p-6 rounded-lg shadow-md">
        <div className="grid grid-cols-2 gap-4">
          <div>
            <h2 className="text-lg font-semibold mb-2">Location</h2>
            <p className="text-gray-600">{restaurant.location}</p>
          </div>
          <div>
            <h2 className="text-lg font-semibold mb-2">Number of Seats</h2>
            <p className="text-gray-600">{restaurant.seats}</p>
          </div>
        </div>
      </div>

      <div className="mt-8">
        <h2 className="text-2xl font-bold mb-4">Meals</h2>
        <Link
          href={`/restaurants/${restaurant.id}/meals/new`}
          className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 mb-4 inline-block"
        >
          Add New Meal
        </Link>
        {/* We'll add the meals list here later */}
      </div>
    </div>
  );
} 