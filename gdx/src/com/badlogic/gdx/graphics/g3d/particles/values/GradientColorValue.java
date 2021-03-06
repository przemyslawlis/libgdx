
package com.badlogic.gdx.graphics.g3d.particles.values;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/** Defines a variation of red, green and blue on a given time line.
 * @author Inferno */
public class GradientColorValue extends ParticleValue {
		static private float[] temp = new float[3];

		private float[] originalColors = {1, 1, 1};
		private float[] colors = {1, 1, 1};
		public float[] timeline = {0};

		public float[] getTimeline () {
			return timeline;
		}

		public void setTimeline (float[] timeline) {
			this.timeline = timeline;
		}

		public float[] getColors () {
			return colors;
		}

		public float[] getOriginalColors () {
			return originalColors;
		}

		public void multiplyColors (Color color) {
			for (int i = 0; i < colors.length; i++) {
				switch (i % 3) {
				case 0:
					colors[i] = originalColors[i] * color.r;
					break;
				case 1:
					colors[i] = originalColors[i] * color.g;
					break;
				case 2:
					colors[i] = originalColors[i] * color.b;
					break;
				}
			}
		}

		public void setColors (float[] colors) {
			this.colors = colors;
		}

		public float[] getColor (float percent) {
			getColor(percent, temp, 0);
			return temp;
		}
		
		public void getColor (float percent, float[] out, int index) {
			int startIndex = 0, endIndex = -1;
			float[] timeline = this.timeline;
			int n = timeline.length;
			for (int i = 1; i < n; i++) {
				float t = timeline[i];
				if (t > percent) {
					endIndex = i;
					break;
				}
				startIndex = i;
			}
			float startTime = timeline[startIndex];
			startIndex *= 3;
			float r1 = colors[startIndex];
			float g1 = colors[startIndex + 1];
			float b1 = colors[startIndex + 2];
			if (endIndex == -1) {
				out[index] = r1;
				out[index + 1] = g1;
				out[index + 2] = b1;
				return;
			}
			float factor = (percent - startTime) / (timeline[endIndex] - startTime);
			endIndex *= 3;
			out[index] = r1 + (colors[endIndex] - r1) * factor;
			out[index + 1] = g1 + (colors[endIndex + 1] - g1) * factor;
			out[index + 2] = b1 + (colors[endIndex + 2] - b1) * factor;
		}
		
		@Override
		public void write (Json json) {
			super.write(json);
			json.writeValue("colors", colors);
			json.writeValue("timeline", timeline);
		}

		@Override
		public void read (Json json, JsonValue jsonData) {
			super.read(json, jsonData);
			colors = json.readValue("colors", float[].class, jsonData);
			originalColors = new float[colors.length];
			System.arraycopy(colors, 0, originalColors, 0, colors.length);
			timeline = json.readValue("timeline", float[].class, jsonData);
		}
		
		public void load (GradientColorValue value) {
			super.load(value);
			colors = new float[value.colors.length];
			System.arraycopy(value.colors, 0, colors, 0, colors.length);
			originalColors = new float[colors.length];
			System.arraycopy(colors, 0, originalColors, 0, colors.length);
			timeline = new float[value.timeline.length];
			System.arraycopy(value.timeline, 0, timeline, 0, timeline.length);
		}
	}