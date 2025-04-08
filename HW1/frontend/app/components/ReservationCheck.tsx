import { UserMeal } from '@/lib/services/api';
import { FormEvent } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";

interface ReservationCheckProps {
  reservation: UserMeal | null;
  onCheck: (code: string) => void;
  onVerify: (id: string) => void;
}

export function ReservationCheck({ reservation, onCheck, onVerify }: ReservationCheckProps) {
  const handleCheck = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    const code = formData.get('code') as string;
    onCheck(code);
  };

  return (
    <Card className="w-full">
      <CardHeader>
        <CardTitle>Check Reservation</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-6">
          <form onSubmit={handleCheck} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="code">Enter your reservation code</Label>
              <Input
                type="text"
                id="code"
                name="code"
                required
                placeholder="Reservation code"
              />
            </div>

            <Button type="submit" className="w-full">
              Check Reservation
            </Button>
          </form>

          {reservation && (
            <div className="space-y-4">
              <h3 className="text-lg font-semibold">Reservation Details</h3>
              <div className="space-y-2">
                <p className="text-sm">
                  <span className="font-medium">Meal:</span> {reservation.meal.name}
                </p>
                <p className="text-sm">
                  <span className="font-medium">Date:</span> {new Date(reservation.meal.date).toLocaleDateString()}
                </p>
                <p className="text-sm">
                  <span className="font-medium">Status:</span>{" "}
                  <Badge variant={reservation.used ? "destructive" : "default"}>
                    {reservation.used ? "Used" : "Valid"}
                  </Badge>
                </p>
              </div>

              {!reservation.used && (
                <Button
                  onClick={() => onVerify(reservation.id)}
                  className="w-full"
                >
                  Mark as Used
                </Button>
              )}
            </div>
          )}
        </div>
      </CardContent>
    </Card>
  );
} 