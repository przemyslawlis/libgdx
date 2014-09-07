
package com.badlogic.gdx.graphics.g3d.particles.values;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray.FloatChannel;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/** Encapsulate the formulas to spawn a particle on a mesh shape.
 * @author Inferno */
public final class UnweightedMeshSpawnShapeValue extends MeshSpawnShapeValue {
	private float[] vertices;
	private short[] indices;
	private int positionOffset, vertexSize, vertexCount, triangleCount;

	public UnweightedMeshSpawnShapeValue (UnweightedMeshSpawnShapeValue value) {
		super(value);
		load(value);
	}

	public UnweightedMeshSpawnShapeValue () {
	}

	@Override
	public void setMesh (Mesh mesh, Model model) {
		super.setMesh(mesh, model);
		vertexSize = mesh.getVertexSize() / 4;
		positionOffset = mesh.getVertexAttribute(Usage.Position).offset / 4;
		int indicesCount = mesh.getNumIndices(), vertexCount = mesh.getNumVertices();
		if (indicesCount > 0) {
			indices = new short[indicesCount];
			mesh.getIndices(indices);
			triangleCount = indices.length / 3;
		} else {
			indices = null;
			triangleCount = vertexCount / 3;
		}
		vertices = new float[vertexCount * vertexSize];
		mesh.getVertices(vertices);
	}

	@Override
	public FloatChannel spawn (FloatChannel positionChannel, int startIndex, int count, float percent) {
		for (int i = startIndex * positionChannel.strideSize, c = i + count * positionChannel.strideSize; i < c; i += positionChannel.strideSize) {
			if (indices == null) {
				// Triangles
				int triangleIndex = MathUtils.random(vertexCount - 3) * vertexSize;
				int p1Offset = triangleIndex + positionOffset, p2Offset = p1Offset + vertexSize, p3Offset = p2Offset + vertexSize;
				float x1 = vertices[p1Offset];
				float y1 = vertices[p1Offset + 1];
				float z1 = vertices[p1Offset + 2];
				float x2 = vertices[p2Offset];
				float y2 = vertices[p2Offset + 1];
				float z2 = vertices[p2Offset + 2];
				float x3 = vertices[p3Offset];
				float y3 = vertices[p3Offset + 1];
				float z3 = vertices[p3Offset + 2];
				Triangle.pick(x1, y1, z1, x2, y2, z2, x3, y3, z3, TMP_V1);
			} else {
				// Indices
				int triangleIndex = MathUtils.random(triangleCount - 1) * 3;
				int p1Offset = indices[triangleIndex] * vertexSize + positionOffset, p2Offset = indices[triangleIndex + 1]
					* vertexSize + positionOffset, p3Offset = indices[triangleIndex + 2] * vertexSize + positionOffset;
				float x1 = vertices[p1Offset];
				float y1 = vertices[p1Offset + 1];
				float z1 = vertices[p1Offset + 2];
				float x2 = vertices[p2Offset];
				float y2 = vertices[p2Offset + 1];
				float z2 = vertices[p2Offset + 2];
				float x3 = vertices[p3Offset];
				float y3 = vertices[p3Offset + 1];
				float z3 = vertices[p3Offset + 2];
				Triangle.pick(x1, y1, z1, x2, y2, z2, x3, y3, z3, TMP_V1);
			}
			if (xOffsetValue.active) TMP_V1.x += xOffsetValue.newLowValue();
			if (yOffsetValue.active) TMP_V1.y += yOffsetValue.newLowValue();
			if (zOffsetValue.active) TMP_V1.z += zOffsetValue.newLowValue();

			positionChannel.data[i + ParticleChannels.XOffset] = TMP_V1.x;
			positionChannel.data[i + ParticleChannels.YOffset] = TMP_V1.y;
			positionChannel.data[i + ParticleChannels.ZOffset] = TMP_V1.z;
		}
		return positionChannel;
	}

	@Override
	public SpawnShapeValue copy () {
		return new UnweightedMeshSpawnShapeValue(this);
	}

}
