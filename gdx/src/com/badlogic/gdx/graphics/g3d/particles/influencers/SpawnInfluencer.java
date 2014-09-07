
package com.badlogic.gdx.graphics.g3d.particles.influencers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray.FloatChannel;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
import com.badlogic.gdx.graphics.g3d.particles.values.PointSpawnShapeValue;
import com.badlogic.gdx.graphics.g3d.particles.values.SpawnShapeValue;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/** It's an {@link Influencer} which controls where the particles will be spawned.
 * @author Inferno */
public class SpawnInfluencer extends Influencer {

	public SpawnShapeValue spawnShapeValue;
	FloatChannel positionChannel;

	public SpawnInfluencer () {
		spawnShapeValue = new PointSpawnShapeValue();
	}

	public SpawnInfluencer (SpawnShapeValue spawnShapeValue) {
		this.spawnShapeValue = spawnShapeValue;
	}

	public SpawnInfluencer (SpawnInfluencer source) {
		spawnShapeValue = source.spawnShapeValue.copy();
	}

	@Override
	public void init () {
		spawnShapeValue.init();
	}

	@Override
	public void allocateChannels () {
		positionChannel = controller.particles.addChannel(ParticleChannels.Position);
	}

	@Override
	public void start () {
		spawnShapeValue.start();
	}

	@Override
	public void activateParticles (int startIndex, int count) {
		spawnShapeValue.spawn(positionChannel, startIndex, count, controller.emitter.percent);
		Matrix4.mulVec(controller.transform.val, positionChannel.data, startIndex * positionChannel.strideSize
			+ ParticleChannels.XOffset, count, positionChannel.strideSize);
		TMP_V1.mul(controller.transform);
	}

	@Override
	public SpawnInfluencer copy () {
		return new SpawnInfluencer(this);
	}

	@Override
	public void write (Json json) {
		json.writeValue("spawnShape", spawnShapeValue, SpawnShapeValue.class);
	}

	@Override
	public void read (Json json, JsonValue jsonData) {
		spawnShapeValue = json.readValue("spawnShape", SpawnShapeValue.class, jsonData);
	}

	@Override
	public void save (AssetManager manager, ResourceData data) {
		spawnShapeValue.save(manager, data);
	}

	@Override
	public void load (AssetManager manager, ResourceData data) {
		spawnShapeValue.load(manager, data);
	}
}
