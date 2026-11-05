package farn.reifnsk_minimap;

import java.awt.Point;

class StripCounter {
	private int count;
	private final Point[] points;

	StripCounter(int size) {
		this.points = new Point[size];
		int x = 0;
		int y = 0;
		int direction = 0;
		int up = 0;
		int down = 0;
		this.points[0] = new Point(x, y);

		for(int index = 1; index < size; ++index) {
			switch(direction) {
			case 0:
				--y;
				break;
			case 1:
				++x;
				break;
			case 2:
				++y;
				break;
			case 3:
				--x;
			}

			++up;
			if(up > down) {
				direction = direction + 1 & 3;
				up = 0;
				if(direction == 0 || direction == 2) {
					++down;
				}
			}

			this.points[index] = new Point(x, y);
		}

	}

	Point next() {
		return this.points[this.count++];
	}

	int count() {
		return this.count;
	}

	void reset() {
		this.count = 0;
	}
}
