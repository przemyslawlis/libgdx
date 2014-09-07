
package com.badlogic.gdx.graphics.g3d.particles.values;

import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray.FloatChannel;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/** Encapsulate the formulas to spawn a particle on a ellipse shape.
 * @author Inferno */
public final class EllipseSpawnShapeValue extends PrimitiveSpawnShapeValue {
	SpawnSide side = SpawnSide.both;

	public EllipseSpawnShapeValue (EllipseSpawnShapeValue value) {
		super(value);
		load(value);
	}

	public EllipseSpawnShapeValue () {
	}

	@Override
	public FloatChannel spawn (FloatChannel positionChannel, int startIndex, int count, float percent) {
		// Generate the point on the surface of the sphere
		float width = spawnWidth + spawnWidthDiff * spawnWidthValue.getScale(percent);
		float height = spawnHeight + spawnHeightDiff * spawnHeightValue.getScale(percent);
		float depth = spawnDepth + spawnDepthDiff * spawnDepthValue.getScale(percent);

		// repeat this one for each spawn shape class
		if (xOffsetValue.active) width += xOffsetValue.newLowValue();
		if (yOffsetValue.active) height += yOffsetValue.newLowValue();
		if (zOffsetValue.active) depth += zOffsetValue.newLowValue();

		float radiusX, radiusY, radiusZ;
		// Where generate the point, on edges or inside ?
		float minT = 0, maxT = MathUtils.PI2;
		if (side == SpawnSide.top) {
			maxT = MathUtils.PI;
		} else if (side == SpawnSide.bottom) {
			maxT = -MathUtils.PI;
		}

		for (int i = startIndex * positionChannel.strideSize, c = i + count * positionChannel.strideSize; i < c; i += positionChannel.strideSize) {
			float t = MathUtils.random(minT, maxT);

			// Where generate the point, on edges or inside ?
			if (edges) {
				if (width == 0) {
					TMP_V1.set(0, height / 2 * MathUtils.sin(t), depth / 2 * MathUtils.cos(t));
				}
				if (height == 0) {
					TMP_V1.set(width / 2 * MathUtils.cos(t), 0, depth / 2 * MathUtils.sin(t));
				}
				if (depth == 0) {
					TMP_V1.set(width / 2 * MathUtils.cos(t), height / 2 * MathUtils.sin(t), 0);
				}

				radiusX = width / 2;
				radiusY = height / 2;
				radiusZ = depth / 2;
			} else {
				radiusX = MathUtils.random(width / 2);
				radiusY = MathUtils.random(height / 2);
				radiusZ = MathUtils.random(depth / 2);
			}

			float z = MathUtils.random(-1, 1f);
			float r = (float)Math.sqrt(1f - z * z);

			positionChannel.data[i + ParticleChannels.XOffset] = radiusX * r * MathUtils.cos(t);
			positionChannel.data[i + ParticleChannels.YOffset] = radiusY * r * MathUtils.sin(t);
			positionChannel.data[i + ParticleChannels.ZOffset] = radiusZ * z;
		}
		return positionChannel;
	}

	public SpawnSide getSide () {
		return side;
	}

	public void setSide (SpawnSide side) {
		this.side = side;
	}

	@Override
	public void load (ParticleValue value) {
		super.load(value);
		EllipseSpawnShapeValue shape = (EllipseSpawnShapeValue)value;
		side = shape.side;
	}

	@Override
	public SpawnShapeValue copy () {
		return new EllipseSpawnShapeValue(this);
	}

	@Override
	public void write (Json json) {
		super.write(json);
		json.writeValue("side", side);
	}

	@Override
	public void read (Json json, JsonValue jsonData) {
		super.read(json, jsonData);
		side = json.readValue("side", SpawnSide.class, jsonData);
	}
}
