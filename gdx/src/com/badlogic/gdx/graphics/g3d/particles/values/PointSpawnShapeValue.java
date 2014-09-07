
package com.badlogic.gdx.graphics.g3d.particles.values;

import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray.FloatChannel;
import com.badlogic.gdx.math.Vector3;

/** Encapsulate the formulas to spawn a particle on a point shape.
 * @author Inferno */
public final class PointSpawnShapeValue extends PrimitiveSpawnShapeValue {

	public PointSpawnShapeValue (PointSpawnShapeValue value) {
		super(value);
		load(value);
	}

	public PointSpawnShapeValue () {
	}

	@Override
	public FloatChannel spawn (FloatChannel positionChannel, int startIndex, int count, float percent) {
		TMP_V1.x = spawnWidth + (spawnWidthDiff * spawnWidthValue.getScale(percent));
		TMP_V1.y = spawnHeight + (spawnHeightDiff * spawnHeightValue.getScale(percent));
		TMP_V1.z = spawnDepth + (spawnDepthDiff * spawnDepthValue.getScale(percent));

		// repeat this one for each spawn shape class
		if (xOffsetValue.active) TMP_V1.x += xOffsetValue.newLowValue();
		if (yOffsetValue.active) TMP_V1.y += yOffsetValue.newLowValue();
		if (zOffsetValue.active) TMP_V1.z += zOffsetValue.newLowValue();

		for (int i = startIndex * positionChannel.strideSize, c = i + count * positionChannel.strideSize; i < c; i += positionChannel.strideSize) {
			positionChannel.data[i + ParticleChannels.XOffset] = TMP_V1.x;
			positionChannel.data[i + ParticleChannels.YOffset] = TMP_V1.y;
			positionChannel.data[i + ParticleChannels.ZOffset] = TMP_V1.z;
		}
		return positionChannel;
	}

	@Override
	public SpawnShapeValue copy () {
		return new PointSpawnShapeValue(this);
	}
}
