
package com.badlogic.gdx.graphics.g3d.particles.values;

import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray.FloatChannel;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/** Encapsulate the formulas to spawn a particle on a rectangle shape.
 * @author Inferno */
public final class RectangleSpawnShapeValue extends PrimitiveSpawnShapeValue {
	public RectangleSpawnShapeValue (RectangleSpawnShapeValue value) {
		super(value);
		load(value);
	}

	public RectangleSpawnShapeValue () {
	}

	@Override
	public FloatChannel spawn (FloatChannel positionChannel, int startIndex, int count, float percent) {
		float width = spawnWidth + (spawnWidthDiff * spawnWidthValue.getScale(percent));
		float height = spawnHeight + (spawnHeightDiff * spawnHeightValue.getScale(percent));
		float depth = spawnDepth + (spawnDepthDiff * spawnDepthValue.getScale(percent));

		// repeat this one for each spawn shape class
		if (xOffsetValue.active) width += xOffsetValue.newLowValue();
		if (yOffsetValue.active) height += yOffsetValue.newLowValue();
		if (zOffsetValue.active) depth += zOffsetValue.newLowValue();

		for (int i = startIndex * positionChannel.strideSize, c = i + count * positionChannel.strideSize; i < c; i += positionChannel.strideSize) {
			// Where generate the point, on edges or inside ?
			if (edges) {
				int a = MathUtils.random(-1, 1);
				float tx = 0, ty = 0, tz = 0;
				if (a == -1) {
					tx = MathUtils.random(1) == 0 ? -width / 2 : width / 2;
					if (tx == 0) {
						ty = MathUtils.random(1) == 0 ? -height / 2 : height / 2;
						tz = MathUtils.random(1) == 0 ? -depth / 2 : depth / 2;
					} else {
						ty = MathUtils.random(height) - height / 2;
						tz = MathUtils.random(depth) - depth / 2;
					}
				} else if (a == 0) {
					// Z
					tz = MathUtils.random(1) == 0 ? -depth / 2 : depth / 2;
					if (tz == 0) {
						ty = MathUtils.random(1) == 0 ? -height / 2 : height / 2;
						tx = MathUtils.random(1) == 0 ? -width / 2 : width / 2;
					} else {
						ty = MathUtils.random(height) - height / 2;
						tx = MathUtils.random(width) - width / 2;
					}
				} else {
					// Y
					ty = MathUtils.random(1) == 0 ? -height / 2 : height / 2;
					if (ty == 0) {
						tx = MathUtils.random(1) == 0 ? -width / 2 : width / 2;
						tz = MathUtils.random(1) == 0 ? -depth / 2 : depth / 2;
					} else {
						tx = MathUtils.random(width) - width / 2;
						tz = MathUtils.random(depth) - depth / 2;
					}
				}
				TMP_V1.x = tx;
				TMP_V1.y = ty;
				TMP_V1.z = tz;
			} else {
				TMP_V1.x = MathUtils.random(width) - width / 2;
				TMP_V1.y = MathUtils.random(height) - height / 2;
				TMP_V1.z = MathUtils.random(depth) - depth / 2;
			}

			positionChannel.data[i + ParticleChannels.XOffset] = TMP_V1.x;
			positionChannel.data[i + ParticleChannels.YOffset] = TMP_V1.y;
			positionChannel.data[i + ParticleChannels.ZOffset] = TMP_V1.z;
		}
		return positionChannel;
	}

	@Override
	public SpawnShapeValue copy () {
		return new RectangleSpawnShapeValue(this);
	}
}
