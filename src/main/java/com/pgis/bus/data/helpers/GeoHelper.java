package com.pgis.bus.data.helpers;

import static java.lang.Math.*;

public class GeoHelper {
	public static double R = 6371.009;

	public static double getDistance(double lat1, double lon1, double lat2,
			double lon2) {
		// First thing to do is setup the constants for the WGS 84 elipsoid
		double a = 6378137.0;
		double b = 6356752.314245;
		double f = (a - b) / a;
		double asquared = a * a;
		double bsquared = b * b;
		double asquaredminusbsquaredoverbsquared = (asquared - bsquared)
				/ bsquared;

		// Convert all our parameters to radians

		double phi1 = lat1 / 180 * PI;
		double phi2 = lat2 / 180 * PI;
		double lambda1 = lon1 / 180 * PI;
		double lambda2 = lon2 / 180 * PI;

		double omega = lambda2 - lambda1;
		double tanphi1 = tan(phi1);
		double tanU1 = (1 - f) * tanphi1;
		double U1 = atan(tanU1);
		double sinU1 = sin(U1);
		double cosU1 = cos(U1);

		double tanphi2 = tan(phi2);
		double tanU2 = (1 - f) * tanphi2;
		double U2 = atan(tanU2);
		double sinU2 = sin(U2);
		double cosU2 = cos(U2);

		double sinU1sinU2 = sinU1 * sinU2;
		double cosU1sinU2 = cosU1 * sinU2;
		double sinU1cosU2 = sinU1 * cosU2;
		double cosU1cosU2 = cosU1 * cosU2;

		double lambda = omega; // Equation 13

		boolean bConverged = false;
		boolean bContinueIteration = true;
		double A = 0;
		double sigma = 0;
		double deltasigma = 0;
		while (bContinueIteration) {
			// Remember the previous value
			double lambda0 = lambda;

			double sinlambda = sin(lambda);
			double coslambda = cos(lambda);

			// Equation 14
			double temp = cosU1sinU2 - sinU1cosU2 * coslambda;
			temp *= temp;
			double sinsquaredsigma = (cosU2 * sinlambda * cosU2 * sinlambda)
					+ temp;
			double sinsigma = sqrt(sinsquaredsigma);

			double cossigma = sinU1sinU2 + (cosU1cosU2 * coslambda); // Equation
																		// 15

			sigma = atan2(sinsigma, cossigma); // Equation 16

			double sinalpha = (sinsquaredsigma == 0) ? 0 : cosU1cosU2
					* sinlambda / sinsigma; // Equation 17

			double alpha = asin(sinalpha);
			double cosalpha = cos(alpha);
			double cossquaredalpha = cosalpha * cosalpha;

			double cos2sigmam = (cossquaredalpha == 0) ? 0 : cossigma - 2
					* sinU1sinU2 / cossquaredalpha; // Equation 18
			double cossquared2sigmam = cos2sigmam * cos2sigmam;

			double usquared = cossquaredalpha
					* asquaredminusbsquaredoverbsquared;

			A = 1
					+ (usquared / 16384)
					* (4096 + usquared
							* (-768 + usquared * (320 - 175 * usquared))); // Equation
																			// 3

			double B = usquared
					/ 1024
					* (256 + usquared
							* (-128 + usquared * (74 - 47 * usquared))); // Equation
																			// 4

			deltasigma = B
					* sinsigma
					* (cos2sigmam + (B / 4)
							* (cossigma * (-1 + 2 * cossquared2sigmam) - B / 6
									* cos2sigmam * (-3 + 4 * sinsquaredsigma)
									* (-3 + 4 * cossquared2sigmam))); // Equation
																		// 6

			double C = f / 16 * cossquaredalpha
					* (4 + f * (4 - 3 * cossquaredalpha)); // Equation 10

			lambda = omega
					+ (1 - C)
					* f
					* sinalpha
					* (sigma + C
							* sinsigma
							* (cos2sigmam + C * cossigma
									* (-1 + 2 * cossquared2sigmam))); // Equation
																		// 11
																		// (modified)

			if (abs(lambda) > PI)
				bContinueIteration = false;
			else {
				bConverged = abs(lambda - lambda0) < 0.0000000000001;
				if (bConverged)
					bContinueIteration = false;
			}
		}

		double s = b * A * (sigma - deltasigma); // Equation 19 (return value
													// will be in meters)

		return s;
	}

	public static double getSphereDistance(double lat1, double lon1, double lat2,
			double lon2) {
		double dlon = abs(lon2 - lon1);
		double x1 = cos(lat2) * sin(dlon);
		double x2 = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dlon);
		double x3 = sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(dlon);

		return R * abs(atan2(sqrt(x1 * x1 + x2 * x2), x3));
	}

	public static double getDistance(double lat1, double lon1, double lat2,
			double lon2, double lat3, double lon3) {
		double d1 = getDistance(lat1, lon1, lat2, lon2);
		double d2 = getDistance(lat2, lon2, lat3, lon3);
		return d1 + d2;
	}
}
