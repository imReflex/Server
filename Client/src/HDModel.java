
public class HDModel extends Model {

	
    public static void nullLoader() {
            modelHeader = null;
            hasAnEdgeToRestrict = null;
            outOfReach = null;
            projected_vertex_y = null;
            projected_vertex_z = null;
            camera_vertex_y = null;
            camera_vertex_x = null;
            camera_vertex_z = null;
            depthListIndices = null;
            faceLists = null;
            anIntArray1673 = null;
            anIntArrayArray1674 = null;
            anIntArray1675 = null;
            anIntArray1676 = null;
            anIntArray1677 = null;
            SINE = null;
            COSINE = null;
            hsl2rgb = null;
            lightDecay = null;
    }
   
    public int modelFormat = 0;
    public HDModel(int modelId)
    {
            byte[] data = modelHeader[modelId].modelData;
            if(data != null && data.length > 1)
            {
                    if (data[data.length - 1] == -1 && data[data.length - 2] == -1)
                    {
                            processCurrentModelData(data, modelId);
                    } else
                    {
                            process508ModelData(data, modelId);            
                            //readOldModel(modelId);
                    }
                    /*if (newmodel[modelId]) {
            			scale2(4);// 2 is too big -- 3 is almost right
            			if (face_render_priorities != null) {
            				for (int j = 0; j < face_render_priorities.length; j++)
            					face_render_priorities[j] = 10;
            			}
            		}*/
                    if (face_render_priorities != null) {
        				for (int j = 0; j < face_render_priorities.length; j++)
        					face_render_priorities[j] = 10;
        			}
            }

    }
   
    public void processCurrentModelData(byte modelData[], int modelId) {
            Stream data = new Stream(modelData);
            Stream data_2 = new Stream(modelData);
            Stream data_3 = new Stream(modelData);
            Stream data_4 = new Stream(modelData);
            Stream data_5 = new Stream(modelData);
            Stream data_6 = new Stream(modelData);
            Stream data_7 = new Stream(modelData);
            data.currentOffset = modelData.length - 23;
            numberOfVerticeCoordinates = data.readUnsignedWord();
            numberOfTriangleFaces = data.readUnsignedWord();
            numberOfTexturedFaces = data.readUnsignedByte();
            int flags = data.readUnsignedByte();
            boolean has_fill_opcode = (flags & 0x1) != 0;
            boolean second_flag = (flags & 0x2) != 0;
            boolean has_vertex_normals = (flags & 0x4) != 0;
            boolean fourth_flag = (flags & 0x8) != 0;
            if(!fourth_flag) {
                    process600ModelData(modelData, modelId);
                    return;
            }
            if (fourth_flag) {
                    data.currentOffset -= 7;
                    modelFormat = data.readUnsignedByte();
                    data.currentOffset += 6;
            }
            int priority = data.readUnsignedByte();
            int alpha_opcode = data.readUnsignedByte();
            int tSkin_opcode = data.readUnsignedByte();
            int texture_opcode = data.readUnsignedByte();
            int i3 = data.readUnsignedByte();
            int j3 = data.readUnsignedWord();
            int k3 = data.readUnsignedWord();
            int l3 = data.readUnsignedWord();
            int i4 = data.readUnsignedWord();
            int j4 = data.readUnsignedWord();
            int particle_index = 0;
            int l4 = 0;
            int particle_color = 0;
            byte G = 0;
           
            int P = 0;
            byte[] J = null;
            byte[] F = null;
            byte[] cb = null;
            byte[] gb = null;
            byte[] lb = null;
            int[] ab = null;
            int[] kb = null;
            int[] y = null;
            int[] N = null;
           
            if (numberOfTexturedFaces > 0)
            {
                    texture_render_type = new byte[numberOfTexturedFaces];
                    data.currentOffset = 0;
                    for (int index = 0; index < numberOfTexturedFaces; index++)
                    {
                            byte opcode = texture_render_type[index] = data.readSignedByte();
                            if (opcode == 0)
                                    particle_index++;
                            if (opcode >= 1 && opcode <= 3)
                                    l4++;
                            if (opcode == 2)
                                    particle_color++;
                    }
            }
            int k5 = numberOfTexturedFaces;
            int vertexModOffset = k5;
            k5 += numberOfVerticeCoordinates;
            int drawTypeBasePos = k5;
            if (has_fill_opcode)
                    k5 += numberOfTriangleFaces;
            int triMeshLinkOffset = k5;
            k5 += numberOfTriangleFaces;
            int facePriorityBasePos = k5;
            if (priority == 255)
                    k5 += numberOfTriangleFaces;
            int tSkinBasePos = k5;
            if (tSkin_opcode == 1)
                    k5 += numberOfTriangleFaces;
            int vSkinBasePos = k5;
            if (i3 == 1)
                    k5 += numberOfVerticeCoordinates;
            int alphaBasePos = k5;
            if (alpha_opcode == 1)
                    k5 += numberOfTriangleFaces;
            int triVPointOffset = k5;
            k5 += i4;
            int texturedTriangleTextureIDBasePos = k5;
            if (texture_opcode == 1)
                    k5 += numberOfTriangleFaces * 2;
            int texturedTriangleIDBasePos = k5;
            k5 += j4;
            int triColorOffset = k5;
            k5 += numberOfTriangleFaces * 2;
            int vertexXOffset = k5;
            k5 += j3;
            int vertexYOffset = k5;
            k5 += k3;
            int vertexZOffset = k5;
            k5 += l3;
            int mainBufferOffset = k5;
            k5 += particle_index * 6;
            int firstBufferOffset = k5;
            k5 += l4 * 6;
            int i_59_ = 6;
            if (modelFormat != 14) {
                    if(modelFormat >= 15) {
                            i_59_ = 9;
                    }
            } else {
                    i_59_ = 7;
            }
            int secondBufferOffset = k5;
            k5 += i_59_ * l4;
            int thirdBufferOffset = k5;
            k5 += l4;
            int fourthBufferOffset = k5;
            k5 += l4;
            int fifthBufferOffset = k5;
            k5 += l4 + particle_color * 2;
           
            verticesXCoordinate = new int[numberOfVerticeCoordinates];
            verticesYCoordinate = new int[numberOfVerticeCoordinates];
            verticesZCoordinate = new int[numberOfVerticeCoordinates];
            face_a = new int[numberOfTriangleFaces];
            face_b = new int[numberOfTriangleFaces];
            face_c = new int[numberOfTriangleFaces];
            vertexVSkin = new int[numberOfVerticeCoordinates];
            //face_render_type = new int[numberOfTriangleFaces];
            face_render_priorities = new int[numberOfTriangleFaces];
            face_alpha = new int[numberOfTriangleFaces];
            triangleTSkin = new int[numberOfTriangleFaces];
            if (i3 == 1)
                    vertexVSkin = new int[numberOfVerticeCoordinates];
            if (has_fill_opcode)
                    face_render_type = new byte[numberOfTriangleFaces];
            if (priority == 255)
                    face_render_priorities = new int[numberOfTriangleFaces];
            else
                    G = (byte) priority;
            if (alpha_opcode == 1)
                    face_alpha = new int[numberOfTriangleFaces];
            if (tSkin_opcode == 1)
                    triangleTSkin = new int[numberOfTriangleFaces];
            if (texture_opcode == 1)
                    face_texture = new short[numberOfTriangleFaces];
            if (texture_opcode == 1 && numberOfTexturedFaces > 0)
                    texture_coordinates = new byte[numberOfTriangleFaces];
            face_color = new short[numberOfTriangleFaces];
            int i_115_ = k5;
            if (numberOfTexturedFaces > 0) {
                    textured_face_a = new int[numberOfTexturedFaces];
                    textured_face_b = new int[numberOfTexturedFaces];
                    textured_face_c = new int[numberOfTexturedFaces];
                    if (l4 > 0) {
                            kb = new int[l4];
                            N = new int[l4];
                            y = new int[l4];
                            gb = new byte[l4];
                            lb = new byte[l4];
                            F = new byte[l4];
                    }
                    if (particle_color > 0) {
                            cb = new byte[particle_color];
                            J = new byte[particle_color];
                    }
            }
            data.currentOffset = vertexModOffset;
            data_2.currentOffset = vertexXOffset;
            data_3.currentOffset = vertexYOffset;
            data_4.currentOffset = vertexZOffset;
            data_5.currentOffset = vSkinBasePos;
            int l10 = 0;
            int i11 = 0;
            int j11 = 0;
            for (int k11 = 0; k11 < numberOfVerticeCoordinates; k11++) {
                    int l11 = data.readUnsignedByte();
                    int j12 = 0;
                    if ((l11 & 1) != 0)
                            j12 = data_2.readSignedSmart();
                    int l12 = 0;
                    if ((l11 & 2) != 0)
                            l12 = data_3.readSignedSmart();
                    int j13 = 0;
                    if ((l11 & 4) != 0)
                            j13 = data_4.readSignedSmart();
                    verticesXCoordinate[k11] = l10 + j12;
                    verticesYCoordinate[k11] = i11 + l12;
                    verticesZCoordinate[k11] = j11 + j13;
                    l10 = verticesXCoordinate[k11];
                    i11 = verticesYCoordinate[k11];
                    j11 = verticesZCoordinate[k11];
                    if (vertexVSkin != null)
                            vertexVSkin[k11] = data_5.readUnsignedByte();
            }
            data.currentOffset = triColorOffset;
            data_2.currentOffset = drawTypeBasePos;
            data_3.currentOffset = facePriorityBasePos;
            data_4.currentOffset = alphaBasePos;
            data_5.currentOffset = tSkinBasePos;
            data_6.currentOffset = texturedTriangleTextureIDBasePos;
            data_7.currentOffset = texturedTriangleIDBasePos;
            for (int triangle = 0; triangle < numberOfTriangleFaces; triangle++)
            {
                    face_color[triangle] = (short) data.readUnsignedWord();
                    if (has_fill_opcode)
                    {
                            face_render_type[triangle] = data_2.readSignedByte();
                    }
                    if (priority == 255)
                            face_render_priorities[triangle] = data_3.readSignedByte();
                   
                    if (alpha_opcode == 1)
                    {
                            face_alpha[triangle] = data_4.readUnsignedByte();
                            if (face_alpha[triangle] < 0)
                                    face_alpha[triangle] = (256 + face_alpha[triangle]);
                    }
                   
                    if (tSkin_opcode == 1)
                            triangleTSkin[triangle] = data_5.readUnsignedByte();
                           
                    if (texture_opcode == 1)
                    {
                            face_texture[triangle] = (short) (data_6.readUnsignedWord() - 1);
                    }
                    if (texture_coordinates != null)
                    {
                            if (face_texture[triangle] == -1)
                            {
                                    texture_coordinates[triangle] = -1;
                            } else
                            {
                                    texture_coordinates[triangle] = (byte) (data_7.readUnsignedByte() - 1);
                            }
                    }
            }
            data.currentOffset = triVPointOffset;
            data_2.currentOffset = triMeshLinkOffset;
            short coordinate_a = 0;
            short coordinate_b = 0;
            short coordinate_c = 0;
            int last_coordinate = 0;
            for (int face = 0; face < numberOfTriangleFaces; face++)
            {
                    int opcode = data_2.readUnsignedByte();
                    if (opcode == 1) {
                            coordinate_a = (short) (data.readSignedSmart() + last_coordinate);
                            last_coordinate = coordinate_a;
                            coordinate_b = (short) (data.readSignedSmart() + last_coordinate);
                            last_coordinate = coordinate_b;
                            coordinate_c = (short) (data.readSignedSmart() + last_coordinate);
                            last_coordinate = coordinate_c;
                            face_a[face] = coordinate_a;
                            face_b[face] = coordinate_b;
                            face_c[face] = coordinate_c;
                    }
                    if (opcode == 2) {
                            coordinate_b = coordinate_c;
                            coordinate_c = (short) (data.readSignedSmart() + last_coordinate);
                            last_coordinate = coordinate_c;
                            face_a[face] = coordinate_a;
                            face_b[face] = coordinate_b;
                            face_c[face] = coordinate_c;
                    }
                    if (opcode == 3) {
                            coordinate_a = coordinate_c;
                            coordinate_c = (short) (data.readSignedSmart() + last_coordinate);
                            last_coordinate = coordinate_c;
                            face_a[face] = coordinate_a;
                            face_b[face] = coordinate_b;
                            face_c[face] = coordinate_c;
                    }
                    if (opcode == 4) {
                            short l14 = coordinate_a;
                            coordinate_a = coordinate_b;
                            coordinate_b = l14;
                            coordinate_c = (short) (data.readSignedSmart() + last_coordinate);
                            last_coordinate = coordinate_c;
                            face_a[face] = coordinate_a;
                            face_b[face] = coordinate_b;
                            face_c[face] = coordinate_c;
                    }
            }
            data.currentOffset = mainBufferOffset;
            data_2.currentOffset = firstBufferOffset;
            data_3.currentOffset = secondBufferOffset;
            data_4.currentOffset = thirdBufferOffset;
            data_5.currentOffset = fourthBufferOffset;
            data_6.currentOffset = fifthBufferOffset;
            for (int face = 0; face < numberOfTexturedFaces; face++)
            {
                    int i15 = texture_render_type[face] & 0xff;
                    if (i15 == 0) {
                            textured_face_a[face] = (short) data.readUnsignedWord();
                            textured_face_b[face] = (short) data.readUnsignedWord();
                            textured_face_c[face] = (short) data.readUnsignedWord();
                    }
                    if (i15 == 1)
                    {
                            textured_face_a[face] = (short) data_2.readUnsignedWord();
                            textured_face_b[face] = (short) data_2.readUnsignedWord();
                            textured_face_c[face] = (short) data_2.readUnsignedWord();
                            if (modelFormat < 15) {
                                    kb[face] = data_3.readUnsignedWord();
                                    if (modelFormat < 14)
                                            N[face] = data_3.readUnsignedWord();
                                    else
                                            N[face] = data_3.read3Bytes();
                                    y[face] = data_3.readUnsignedWord();
                            } else {
                                    kb[face] = data_3.read3Bytes();
                                    N[face] = data_3.read3Bytes();
                                    y[face] = data_3.read3Bytes();
                            }
                            gb[face] = data_4.readSignedByte();
                            lb[face] = data_5.readSignedByte();
                            F[face] = data_6.readSignedByte();
                    }
                    if (i15 == 2)
                    {
                            textured_face_a[face] = (short) data_2.readUnsignedWord();
                            textured_face_b[face] = (short) data_2.readUnsignedWord();
                            textured_face_c[face] = (short) data_2.readUnsignedWord();
                            if (modelFormat < 15) {
                                    kb[face] = data_3.readUnsignedWord();
                                    if (modelFormat < 14)
                                            N[face] = data_3.readUnsignedWord();
                                    else
                                            N[face] = data_3.read3Bytes();
                                    y[face] = data_3.readUnsignedWord();
                            } else {
                                    kb[face] = data_3.read3Bytes();
                                    N[face] = data_3.read3Bytes();
                                    y[face] = data_3.read3Bytes();
                            }
                            gb[face] = data_4.readSignedByte();
                            lb[face] = data_5.readSignedByte();
                            F[face] = data_6.readSignedByte();
                            cb[face] = data_6.readSignedByte();
                            J[face] = data_6.readSignedByte();
                    }
                    if (i15 == 3)
                    {
                            textured_face_a[face] = (short) data_2.readUnsignedWord();
                            textured_face_b[face] = (short) data_2.readUnsignedWord();
                            textured_face_c[face] = (short) data_2.readUnsignedWord();
                            if (modelFormat < 15) {
                                    kb[face] = data_3.readUnsignedWord();
                                    if (modelFormat < 14)
                                            N[face] = data_3.readUnsignedWord();
                                    else
                                            N[face] = data_3.read3Bytes();
                                    y[face] = data_3.readUnsignedWord();
                            } else {
                                    kb[face] = data_3.read3Bytes();
                                    N[face] = data_3.read3Bytes();
                                    y[face] = data_3.read3Bytes();
                            }
                            gb[face] = data_4.readSignedByte();
                            lb[face] = data_5.readSignedByte();
                            F[face] = data_6.readSignedByte();
                    }
            }
            //if (priority != 255)
                    //if (face_render_priorities != null)
                            //for(int i12 = 0; i12 != numberOfTriangleFaces; i12++)
                                    //face_render_priorities[i12] = priority;
            data.currentOffset = k5;
            int priorityIndex;
            int triangleIndex;
            int triangleFace;
            if(second_flag) {
                    triangleIndex = data.readUnsignedByte();
                    for(triangleFace = 0; triangleFace != triangleIndex; triangleFace++) {
                            int unknownInt = data.readUnsignedWord();
                            priorityIndex = data.readUnsignedWord();
                            byte face_priority;
                            if(priority != -1)
                                    face_priority = (byte)priority;
                            else
                                    face_priority = (byte)face_render_priorities[priorityIndex];
                    }
                    triangleFace = data.readUnsignedByte();
                    if(triangleFace > 0)
                            for(int unknownInt = 0; unknownInt != triangleFace; unknownInt++) {
                                    priorityIndex = data.readUnsignedWord();
                                    int unknownInt2 = data.readUnsignedWord();
                            }
           
                    if(has_vertex_normals) {
                            triangleIndex = data.readUnsignedByte();
                            if(triangleIndex > 0)
                                    for(triangleFace = 0; triangleFace != triangleIndex; triangleFace++) {
                                            int unknownInt = data.readUnsignedWord();
                                            priorityIndex = data.readUnsignedWord();
                                            int unknownInt2 = data.readUnsignedByte();
                                            byte unknownByte = data.readSignedByte();
                                    }
                    }
            }
            if(modelFormat > 13) {
                    if(modelFormat >= 15)
                            if(face_render_priorities != null) {
                                    for (int count = 0; count != numberOfTriangleFaces; ++count)
                                            if (face_render_priorities[count] >= 10) {
                                                    face_render_priorities = null;
                                                    break;
                                            }
                                    if (face_priority > 10 || (face_priority == -1 && face_render_priorities == null))
                                            face_priority = 10;
                            }
            } else {
                    upscale(2);
            }
            scaleT(32,32,32);
            translate(0, 6, 0);
    }
   
    public void process600ModelData(byte modelData[], int modelId) {
            Stream data = new Stream(modelData);
            Stream data_2 = new Stream(modelData);
            Stream data_3 = new Stream(modelData);
            Stream data_4 = new Stream(modelData);
            Stream data_5 = new Stream(modelData);
            Stream data_6 = new Stream(modelData);
            Stream data_7 = new Stream(modelData);
            data.currentOffset = modelData.length - 23;
            numberOfVerticeCoordinates = data.readUnsignedWord();
            numberOfTriangleFaces = data.readUnsignedWord();
            numberOfTexturedFaces = data.readUnsignedByte();
            int flags = data.readUnsignedByte();
            boolean displayType = (flags & 0x1) != 0;
            boolean bool_78_ = (flags & 0x2) != 0;
            boolean bool_25_ = (flags & 0x4) != 0;
            boolean versionFormat = (flags & 0x8) != 0;
           
            if (!versionFormat) {
                    proces525ModelData(modelData, modelId);
                    return;
            }
            if (versionFormat) {
                    data.currentOffset -= 7;
                    modelFormat = data.readUnsignedByte();
                    data.currentOffset += 6;
            }
            int priority = data.readUnsignedByte();
            int j2 = data.readUnsignedByte();
            int k2 = data.readUnsignedByte();
            int l2 = data.readUnsignedByte();
            int i3 = data.readUnsignedByte();
            int j3 = data.readUnsignedWord();
            int k3 = data.readUnsignedWord();
            int l3 = data.readUnsignedWord();
            int i4 = data.readUnsignedWord();
            int j4 = data.readUnsignedWord();
            int particle_index = 0;
            int l4 = 0;
            int i5 = 0;
            int v = 0;
            int hb = 0;
            int P = 0;
            byte G = 0;
            byte[] J = null;
            byte[] F = null;
            byte[] cb = null;
            byte[] gb = null;
            byte[] lb = null;
            int[] ab = null;
            int[] kb = null;
            int[] y = null;
            int[] N = null;
            if (numberOfTexturedFaces > 0) {
                    texture_render_type = new byte[numberOfTexturedFaces];
                    data.currentOffset = 0;
                    for (int j5 = 0; j5 < numberOfTexturedFaces; j5++) {
                            byte byte0 = texture_render_type[j5] = data.readSignedByte();
                            if (byte0 == 0)
                                    particle_index++;
                            if (byte0 >= 1 && byte0 <= 3)
                                    l4++;
                            if (byte0 == 2)
                                    i5++;
                    }
            }
            int k5 = numberOfTexturedFaces;
            int l5 = k5;
            k5 += numberOfVerticeCoordinates;
            int i6 = k5;
            if (displayType)
                    k5 += numberOfTriangleFaces;
            if (flags == 1)
                    k5 += numberOfTriangleFaces;
            int j6 = k5;
            k5 += numberOfTriangleFaces;
            int k6 = k5;
            if (priority == 255)
                    k5 += numberOfTriangleFaces;
            int l6 = k5;
            if (k2 == 1)
                    k5 += numberOfTriangleFaces;
            int i7 = k5;
            if (i3 == 1)
                    k5 += numberOfVerticeCoordinates;
            int j7 = k5;
            if (j2 == 1)
                    k5 += numberOfTriangleFaces;
            int k7 = k5;
            k5 += i4;
            int l7 = k5;
            if (l2 == 1)
                    k5 += numberOfTriangleFaces * 2;
            int i8 = k5;
            k5 += j4;
            int j8 = k5;
            k5 += numberOfTriangleFaces * 2;
            int k8 = k5;
            k5 += j3;
            int l8 = k5;
            k5 += k3;
            int i9 = k5;
            k5 += l3;
            int j9 = k5;
            k5 += particle_index * 6;
            int k9 = k5;
            k5 += l4 * 6;
            int i_59_ = 6;
            if (modelFormat != 14) {
                    if (modelFormat >= 15)
                            i_59_ = 9;
            } else
                    i_59_ = 7;
            int l9 = k5;
            k5 += i_59_ * l4;
            int i10 = k5;
            k5 += l4;
            int j10 = k5;
            k5 += l4;
            int k10 = k5;
            k5 += l4 + i5 * 2;
            v = numberOfVerticeCoordinates;
            hb = numberOfTriangleFaces;
            P = numberOfTexturedFaces;
            verticesXCoordinate = new int[numberOfVerticeCoordinates];
            verticesYCoordinate = new int[numberOfVerticeCoordinates];
            verticesZCoordinate = new int[numberOfVerticeCoordinates];
            face_a = new int[numberOfTriangleFaces];
            face_b = new int[numberOfTriangleFaces];
            face_c = new int[numberOfTriangleFaces];
            vertexVSkin = new int[numberOfVerticeCoordinates];
            //face_render_type = new int[numberOfTriangleFaces];
            face_render_priorities = new int[numberOfTriangleFaces];
            face_alpha = new int[numberOfTriangleFaces];
            triangleTSkin = new int[numberOfTriangleFaces];
            if (i3 == 1)
                    vertexVSkin = new int[numberOfVerticeCoordinates];
            if (displayType)
                    face_render_type = new byte[numberOfTriangleFaces];
            if (priority == 255)
                    face_render_priorities = new int[numberOfTriangleFaces];
            else
                    G = (byte) priority;
            if (j2 == 1)
                    face_alpha = new int[numberOfTriangleFaces];
            if (k2 == 1)
                    triangleTSkin = new int[numberOfTriangleFaces];
            if (l2 == 1)
                    face_texture = new short[numberOfTriangleFaces];
            if (l2 == 1 && numberOfTexturedFaces > 0)
                    texture_coordinates = new byte[numberOfTriangleFaces];
            face_color = new short[numberOfTriangleFaces];
            int i_115_ = k5;
            if (numberOfTexturedFaces > 0) {
                    textured_face_a = new int[numberOfTexturedFaces];
                    textured_face_b = new int[numberOfTexturedFaces];
                    textured_face_c = new int[numberOfTexturedFaces];
                    if (l4 > 0) {
                            kb = new int[l4];
                            N = new int[l4];
                            y = new int[l4];
                            gb = new byte[l4];
                            lb = new byte[l4];
                            F = new byte[l4];
                    }
                    if (i5 > 0) {
                            cb = new byte[i5];
                            J = new byte[i5];
                    }
            }
            data.currentOffset = l5;
            data_2.currentOffset = k8;
            data_3.currentOffset = l8;
            data_4.currentOffset = i9;
            data_5.currentOffset = i7;
            int l10 = 0;
            int i11 = 0;
            int j11 = 0;
            for (int k11 = 0; k11 < numberOfVerticeCoordinates; k11++) {
                    int l11 = data.readUnsignedByte();
                    int j12 = 0;
                    if ((l11 & 1) != 0)
                            j12 = data_2.readSignedSmart();
                    int l12 = 0;
                    if ((l11 & 2) != 0)
                            l12 = data_3.readSignedSmart();
                    int j13 = 0;
                    if ((l11 & 4) != 0)
                            j13 = data_4.readSignedSmart();
                    verticesXCoordinate[k11] = l10 + j12;
                    verticesYCoordinate[k11] = i11 + l12;
                    verticesZCoordinate[k11] = j11 + j13;
                    l10 = verticesXCoordinate[k11];
                    i11 = verticesYCoordinate[k11];
                    j11 = verticesZCoordinate[k11];
                    if (vertexVSkin != null)
                            vertexVSkin[k11] = data_5.readUnsignedByte();
            }
            data.currentOffset = j8;
            data_2.currentOffset = i6;
            data_3.currentOffset = k6;
            data_4.currentOffset = j7;
            data_5.currentOffset = l6;
            data_6.currentOffset = l7;
            data_7.currentOffset = i8;
            for (int triangle = 0; triangle < numberOfTriangleFaces; triangle++) {
                    face_color[triangle] = (short) data.readUnsignedWord();
                    if (flags == 1)
                    {
                            face_render_type[triangle] = data_2.readSignedByte();
                    }
                    if (priority == 255)
                            face_render_priorities[triangle] = data_3.readSignedByte();
                   
                    if (j2 == 1)
                    {
                            face_alpha[triangle] = data_4.readSignedByte();
                            if (face_alpha[triangle] < 0)
                                    face_alpha[triangle] = (256 + face_alpha[triangle]);
                    }
                   
                    if (k2 == 1)
                            triangleTSkin[triangle] = data_5.readUnsignedByte();
                           
                    if (l2 == 1)
                    {
                            face_texture[triangle] = (short) (data_6.readUnsignedWord() - 1);
                    }
                    if (texture_coordinates != null)
                    {
                            if (face_texture[triangle] == -1)
                            {
                                    texture_coordinates[triangle] = -1;
                            } else
                            {
                                    texture_coordinates[triangle] = (byte) (data_7.readUnsignedByte() - 1);
                            }
                    }
            }
            data.currentOffset = k7;
            data_2.currentOffset = j6;
            short k12 = 0;
            short i13 = 0;
            short k13 = 0;
            int l13 = 0;
            for (int i14 = 0; i14 < numberOfTriangleFaces; i14++)
            {
                    int opcode = data_2.readUnsignedByte();
                    if (opcode == 1) {
                            k12 = (short) (data.readSignedSmart() + l13);
                            l13 = k12;
                            i13 = (short) (data.readSignedSmart() + l13);
                            l13 = i13;
                            k13 = (short) (data.readSignedSmart() + l13);
                            l13 = k13;
                            face_a[i14] = k12;
                            face_b[i14] = i13;
                            face_c[i14] = k13;
                    }
                    if (opcode == 2) {
                            i13 = k13;
                            k13 = (short) (data.readSignedSmart() + l13);
                            l13 = k13;
                            face_a[i14] = k12;
                            face_b[i14] = i13;
                            face_c[i14] = k13;
                    }
                    if (opcode == 3) {
                            k12 = k13;
                            k13 = (short) (data.readSignedSmart() + l13);
                            l13 = k13;
                            face_a[i14] = k12;
                            face_b[i14] = i13;
                            face_c[i14] = k13;
                    }
                    if (opcode == 4) {
                            short l14 = k12;
                            k12 = i13;
                            i13 = l14;
                            k13 = (short) (data.readSignedSmart() + l13);
                            l13 = k13;
                            face_a[i14] = k12;
                            face_b[i14] = i13;
                            face_c[i14] = k13;
                    }
            }
            data.currentOffset = j9;
            data_2.currentOffset = k9;
            data_3.currentOffset = l9;
            data_4.currentOffset = i10;
            data_5.currentOffset = j10;
            data_6.currentOffset = k10;
            for (int face = 0; face < numberOfTexturedFaces; face++) {
                    int i15 = texture_render_type[face] & 0xff;
                    if (i15 == 0) {
                            textured_face_a[face] = (short) data.readUnsignedWord();
                            textured_face_b[face] = (short) data.readUnsignedWord();
                            textured_face_c[face] = (short) data.readUnsignedWord();
                    }
                    if (i15 == 1) {
                            textured_face_a[face] = (short) data_2.readUnsignedWord();
                            textured_face_b[face] = (short) data_2.readUnsignedWord();
                            textured_face_c[face] = (short) data_2.readUnsignedWord();
                            if (modelFormat < 15) {
                                    kb[face] = data_3.readUnsignedWord();
                                    if (modelFormat >= 14)
                                            N[face] = data_3.v(-1);
                                    else
                                            N[face] = data_3.readUnsignedWord();
                                    y[face] = data_3.readUnsignedWord();
                            } else {
                                    kb[face] = data_3.v(-1);
                                    N[face] = data_3.v(-1);
                                    y[face] = data_3.v(-1);
                            }
                            gb[face] = data_4.readSignedByte();
                            lb[face] = data_5.readSignedByte();
                            F[face] = data_6.readSignedByte();
                    }
                    if (i15 == 2) {
                            textured_face_a[face] = (short) data_2.readUnsignedWord();
                            textured_face_b[face] = (short) data_2.readUnsignedWord();
                            textured_face_c[face] = (short) data_2.readUnsignedWord();
                            if (modelFormat >= 15) {
                                    kb[face] = data_3.v(-1);
                                    N[face] = data_3.v(-1);
                                    y[face] = data_3.v(-1);
                            } else {
                                    kb[face] = data_3.readUnsignedWord();
                                    if (modelFormat < 14)
                                            N[face] = data_3.readUnsignedWord();
                                    else
                                            N[face] = data_3.v(-1);
                                    y[face] = data_3.readUnsignedWord();
                            }
                            gb[face] = data_4.readSignedByte();
                            lb[face] = data_5.readSignedByte();
                            F[face] = data_6.readSignedByte();
                            cb[face] = data_6.readSignedByte();
                            J[face] = data_6.readSignedByte();
                    }
                    if (i15 == 3) {
                            textured_face_a[face] = (short) data_2.readUnsignedWord();
                            textured_face_b[face] = (short) data_2.readUnsignedWord();
                            textured_face_c[face] = (short) data_2.readUnsignedWord();
                            if (modelFormat < 15) {
                                    kb[face] = data_3.readUnsignedWord();
                                    if (modelFormat < 14)
                                            N[face] = data_3.readUnsignedWord();
                                    else
                                            N[face] = data_3.v(-1);
                                    y[face] = data_3.readUnsignedWord();
                            } else {
                                    kb[face] = data_3.v(-1);
                                    N[face] = data_3.v(-1);
                                    y[face] = data_3.v(-1);
                            }
                            gb[face] = data_4.readSignedByte();
                            lb[face] = data_5.readSignedByte();
                            F[face] = data_6.readSignedByte();
                    }
            }
            scaleT(32,32,32);
    }
   
    public void proces525ModelData(byte abyte0[], int modelID) {
            Stream nc1 = new Stream(abyte0);
            Stream nc2 = new Stream(abyte0);
            Stream nc3 = new Stream(abyte0);
            Stream nc4 = new Stream(abyte0);
            Stream nc5 = new Stream(abyte0);
            Stream nc6 = new Stream(abyte0);
            Stream nc7 = new Stream(abyte0);
            nc1.currentOffset = abyte0.length - 23;
            numberOfVerticeCoordinates = nc1.readUnsignedWord();
            numberOfTriangleFaces = nc1.readUnsignedWord();
            numberOfTexturedFaces = nc1.readUnsignedByte();
            int l1 = nc1.readUnsignedByte();
            boolean bool = (0x1 & l1 ^ 0xffffffff) == -2;
            boolean bool_78_ = (l1 & 0x2 ^ 0xffffffff) == -3;
            int priority = nc1.readUnsignedByte();
            int j2 = nc1.readUnsignedByte();
            int k2 = nc1.readUnsignedByte();
            int l2 = nc1.readUnsignedByte();
            int i3 = nc1.readUnsignedByte();
            int j3 = nc1.readUnsignedWord();
            int k3 = nc1.readUnsignedWord();
            int l3 = nc1.readUnsignedWord();
            int i4 = nc1.readUnsignedWord();
            int j4 = nc1.readUnsignedWord();
            int particle_index = 0;
            int l4 = 0;
            int i5 = 0;
            int v = 0;
            int hb = 0;
            int P = 0;
            byte G = 0;
            byte[] J = null;
            byte[] F = null;
            byte[] cb = null;
            byte[] gb = null;
            byte[] lb = null;
            int[] ab = null;
            int[] kb = null;
            int[] y = null;
            int[] N = null;
            if (numberOfTexturedFaces > 0) {
                    texture_render_type = new byte[numberOfTexturedFaces];
                    nc1.currentOffset = 0;
                    for (int j5 = 0; j5 < numberOfTexturedFaces; j5++) {
                            byte byte0 = texture_render_type[j5] = nc1.readSignedByte();
                            if (byte0 == 0)
                                    particle_index++;
                            if (byte0 >= 1 && byte0 <= 3)
                                    l4++;
                            if (byte0 == 2)
                                    i5++;
                    }
            }
            int k5 = numberOfTexturedFaces;
            int l5 = k5;
            k5 += numberOfVerticeCoordinates;
            int i6 = k5;
            if (l1 == 1)
                    k5 += numberOfTriangleFaces;
            int j6 = k5;
            k5 += numberOfTriangleFaces;
            int k6 = k5;
            if (priority == 255)
                    k5 += numberOfTriangleFaces;
            int l6 = k5;
            if (k2 == 1)
                    k5 += numberOfTriangleFaces;
            int i7 = k5;
            if (i3 == 1)
                    k5 += numberOfVerticeCoordinates;
            int j7 = k5;
            if (j2 == 1)
                    k5 += numberOfTriangleFaces;
            int k7 = k5;
            k5 += i4;
            int l7 = k5;
            if (l2 == 1)
                    k5 += numberOfTriangleFaces * 2;
            int i8 = k5;
            k5 += j4;
            int j8 = k5;
            k5 += numberOfTriangleFaces * 2;
            int k8 = k5;
            k5 += j3;
            int l8 = k5;
            k5 += k3;
            int i9 = k5;
            k5 += l3;
            int j9 = k5;
            k5 += particle_index * 6;
            int k9 = k5;
            k5 += l4 * 6;
            int l9 = k5;
            k5 += l4 * 6;
            int i10 = k5;
            k5 += l4;
            int j10 = k5;
            k5 += l4;
            int k10 = k5;
            k5 += l4 + i5 * 2;
            verticesXCoordinate = new int[numberOfVerticeCoordinates];
            verticesYCoordinate = new int[numberOfVerticeCoordinates];
            verticesZCoordinate = new int[numberOfVerticeCoordinates];
            face_a = new int[numberOfTriangleFaces];
            face_b = new int[numberOfTriangleFaces];
            face_c = new int[numberOfTriangleFaces];
            vertexVSkin = new int[numberOfVerticeCoordinates];
            //face_render_type = new int[numberOfTriangleFaces];
            face_render_priorities = new int[numberOfTriangleFaces];
            face_alpha = new int[numberOfTriangleFaces];
            triangleTSkin = new int[numberOfTriangleFaces];
            if (i3 == 1)
                    vertexVSkin = new int[numberOfVerticeCoordinates];
            if (bool)
                    face_render_type = new byte[numberOfTriangleFaces];
            if (priority == 255)
                    face_render_priorities = new int[numberOfTriangleFaces];
            else
                    G = (byte) priority;
            if (j2 == 1)
                    face_alpha = new int[numberOfTriangleFaces];
            if (k2 == 1)
                    triangleTSkin = new int[numberOfTriangleFaces];
            if (l2 == 1)
                    face_texture = new short[numberOfTriangleFaces];
            if (l2 == 1 && numberOfTexturedFaces > 0)
                    texture_coordinates = new byte[numberOfTriangleFaces];
            face_color = new short[numberOfTriangleFaces];
            int i_115_ = k5;
            if (numberOfTexturedFaces > 0) {
                    textured_face_a = new int[numberOfTexturedFaces];
                    textured_face_b = new int[numberOfTexturedFaces];
                    textured_face_c = new int[numberOfTexturedFaces];
                    if (l4 > 0) {
                            kb = new int[l4];
                            N = new int[l4];
                            y = new int[l4];
                            gb = new byte[l4];
                            lb = new byte[l4];
                            F = new byte[l4];
                    }
                    if (i5 > 0) {
                            cb = new byte[i5];
                            J = new byte[i5];
                    }
            }
            nc1.currentOffset = l5;
            nc2.currentOffset = k8;
            nc3.currentOffset = l8;
            nc4.currentOffset = i9;
            nc5.currentOffset = i7;
            int l10 = 0;
            int i11 = 0;
            int j11 = 0;
            for (int k11 = 0; k11 < numberOfVerticeCoordinates; k11++) {
                    int l11 = nc1.readUnsignedByte();
                    int j12 = 0;
                    if ((l11 & 1) != 0)
                            j12 = nc2.readSignedSmart();
                    int l12 = 0;
                    if ((l11 & 2) != 0)
                            l12 = nc3.readSignedSmart();
                    int j13 = 0;
                    if ((l11 & 4) != 0)
                            j13 = nc4.readSignedSmart();
                    verticesXCoordinate[k11] = l10 + j12;
                    verticesYCoordinate[k11] = i11 + l12;
                    verticesZCoordinate[k11] = j11 + j13;
                    l10 = verticesXCoordinate[k11];
                    i11 = verticesYCoordinate[k11];
                    j11 = verticesZCoordinate[k11];
                    if (vertexVSkin != null)
                            vertexVSkin[k11] = nc5.readUnsignedByte();
            }
            nc1.currentOffset = j8;
            nc2.currentOffset = i6;
            nc3.currentOffset = k6;
            nc4.currentOffset = j7;
            nc5.currentOffset = l6;
            nc6.currentOffset = l7;
            nc7.currentOffset = i8;
            for (int slot = 0; slot < numberOfTriangleFaces; slot++) {
                    face_color[slot] = (short) nc1.readUnsignedWord();
                    if (l1 == 1) {
                            face_render_type[slot] = nc2.readSignedByte();
                    }
                    if (priority == 255) {
                            face_render_priorities[slot] = nc3.readSignedByte();
                    }
                    if (j2 == 1) {
                            face_alpha[slot] = nc4.readSignedByte();
                            if (face_alpha[slot] < 0)
                                    face_alpha[slot] = (256 + face_alpha[slot]);
                    }
                    if (k2 == 1)
                            triangleTSkin[slot] = nc5.readUnsignedByte();
                           
                    if (l2 == 1)
                            face_texture[slot] = (short) (nc6.readUnsignedWord() - 1);
                           
                    if (texture_coordinates != null)
                            if (face_texture[slot] == -1)
                                    texture_coordinates[slot] = -1;
                            else
                                    texture_coordinates[slot] = (byte) (nc7.readUnsignedByte() - 1);
            }
            nc1.currentOffset = k7;
            nc2.currentOffset = j6;
            short k12 = 0;
            short i13 = 0;
            short k13 = 0;
            int l13 = 0;
            for (int i14 = 0; i14 < numberOfTriangleFaces; i14++)
            {
                    int opcode = nc2.readUnsignedByte();
                    if (opcode == 1) {
                            k12 = (short) (nc1.readSignedSmart() + l13);
                            l13 = k12;
                            i13 = (short) (nc1.readSignedSmart() + l13);
                            l13 = i13;
                            k13 = (short) (nc1.readSignedSmart() + l13);
                            l13 = k13;
                            face_a[i14] = k12;
                            face_b[i14] = i13;
                            face_c[i14] = k13;
                    }
                    if (opcode == 2) {
                            i13 = k13;
                            k13 = (short) (nc1.readSignedSmart() + l13);
                            l13 = k13;
                            face_a[i14] = k12;
                            face_b[i14] = i13;
                            face_c[i14] = k13;
                    }
                    if (opcode == 3) {
                            k12 = k13;
                            k13 = (short) (nc1.readSignedSmart() + l13);
                            l13 = k13;
                            face_a[i14] = k12;
                            face_b[i14] = i13;
                            face_c[i14] = k13;
                    }
                    if (opcode == 4) {
                            short l14 = k12;
                            k12 = i13;
                            i13 = l14;
                            k13 = (short) (nc1.readSignedSmart() + l13);
                            l13 = k13;
                            face_a[i14] = k12;
                            face_b[i14] = i13;
                            face_c[i14] = k13;
                    }
            }
            nc1.currentOffset = j9;
            nc2.currentOffset = k9;
            nc3.currentOffset = l9;
            nc4.currentOffset = i10;
            nc5.currentOffset = j10;
            nc6.currentOffset = k10;
            for (int face = 0; face < numberOfTexturedFaces; face++) {
                    int i15 = texture_render_type[face] & 0xff;
                    if (i15 == 0) {
                            textured_face_a[face] = (short) nc1.readUnsignedWord();
                            textured_face_b[face] = (short) nc1.readUnsignedWord();
                            textured_face_c[face] = (short) nc1.readUnsignedWord();
                    }
                    if (i15 == 1) {
                            textured_face_a[face] = (short) nc2.readUnsignedWord();
                            textured_face_b[face] = (short) nc2.readUnsignedWord();
                            textured_face_c[face] = (short) nc2.readUnsignedWord();
                            kb[face] = nc3.readUnsignedWord();
                            N[face] = nc3.readUnsignedWord();
                            y[face] = nc3.readUnsignedWord();
                            gb[face] = nc4.readSignedByte();
                            lb[face] = nc5.readSignedByte();
                            F[face] = nc6.readSignedByte();
                    }
                    if (i15 == 2) {
                            textured_face_a[face] = (short) nc2.readUnsignedWord();
                            textured_face_b[face] = (short) nc2.readUnsignedWord();
                            textured_face_c[face] = (short) nc2.readUnsignedWord();
                            kb[face] = nc3.readUnsignedWord();
                            N[face] = nc3.readUnsignedWord();
                            y[face] = nc3.readUnsignedWord();
                            gb[face] = nc4.readSignedByte();
                            lb[face] = nc5.readSignedByte();
                            F[face] = nc6.readSignedByte();
                            cb[face] = nc6.readSignedByte();
                            J[face] = nc6.readSignedByte();
                    }
                    if (i15 == 3) {
                            textured_face_a[face] = (short) nc2.readUnsignedWord();
                            textured_face_b[face] = (short) nc2.readUnsignedWord();
                            textured_face_c[face] = (short) nc2.readUnsignedWord();
                            kb[face] = nc3.readUnsignedWord();
                            N[face] = nc3.readUnsignedWord();
                            y[face] = nc3.readUnsignedWord();
                            gb[face] = nc4.readSignedByte();
                            lb[face] = nc5.readSignedByte();
                            F[face] = nc6.readSignedByte();
                    }
            }
    }
   
    public void process508ModelData(byte[] modelData, int modelId) {
            boolean has_face_type = false;
            boolean has_texture_type = false;
            Stream stream = new Stream(modelData);
            Stream stream1 = new Stream(modelData);
            Stream stream2 = new Stream(modelData);
            Stream stream3 = new Stream(modelData);
            Stream stream4 = new Stream(modelData);
            stream.currentOffset = modelData.length - 18;
            numberOfVerticeCoordinates = stream.readUnsignedWord();
            numberOfTriangleFaces = stream.readUnsignedWord();
            numberOfTexturedFaces = stream.readUnsignedByte();
            int i_249_ = stream.readUnsignedByte();
            int i_250_ = stream.readUnsignedByte();
            int i_251_ = stream.readUnsignedByte();
            int i_252_ = stream.readUnsignedByte();
            int i_253_ = stream.readUnsignedByte();
            int i_254_ = stream.readUnsignedWord();
            int i_255_ = stream.readUnsignedWord();
            int i_256_ = stream.readUnsignedWord();
            int i_257_ = stream.readUnsignedWord();
            int i_258_ = 0;
            int i_259_ = i_258_;
            i_258_ += numberOfVerticeCoordinates;
            int i_260_ = i_258_;
            i_258_ += numberOfTriangleFaces;
            int i_261_ = i_258_;
            if (i_250_ == 255)
                    i_258_ += numberOfTriangleFaces;
            int i_262_ = i_258_;
            if (i_252_ == 1)
                    i_258_ += numberOfTriangleFaces;
            int i_263_ = i_258_;
            if (i_249_ == 1)
                    i_258_ += numberOfTriangleFaces;
            int i_264_ = i_258_;
            if (i_253_ == 1)
                    i_258_ += numberOfVerticeCoordinates;
            int i_265_ = i_258_;
            if (i_251_ == 1)
                    i_258_ += numberOfTriangleFaces;
            int i_266_ = i_258_;
            i_258_ += i_257_;
            int i_267_ = i_258_;
            i_258_ += numberOfTriangleFaces * 2;
            int i_268_ = i_258_;
            i_258_ += numberOfTexturedFaces * 6;
            int i_269_ = i_258_;
            i_258_ += i_254_;
            int i_270_ = i_258_;
            i_258_ += i_255_;
            int i_271_ = i_258_;
            i_258_ += i_256_;
            byte G = 0;
            verticesXCoordinate = new int[numberOfVerticeCoordinates];
            verticesYCoordinate = new int[numberOfVerticeCoordinates];
            verticesZCoordinate = new int[numberOfVerticeCoordinates];
            face_a = new int[numberOfTriangleFaces];
            face_b = new int[numberOfTriangleFaces];
            face_c = new int[numberOfTriangleFaces];
            if (numberOfTexturedFaces > 0) {
                    texture_render_type = new byte[numberOfTexturedFaces];
                    textured_face_a = new int[numberOfTexturedFaces];
                    textured_face_b = new int[numberOfTexturedFaces];
                    textured_face_c = new int[numberOfTexturedFaces];
            }
            if (i_253_ == 1)
                    vertexVSkin = new int[numberOfVerticeCoordinates];
            if (i_249_ == 1) {
                    face_render_type = new byte[numberOfTriangleFaces];
                    texture_coordinates = new byte[numberOfTriangleFaces];
                    face_texture = new short[numberOfTriangleFaces];
            }
            if (i_250_ == 255)
                    face_render_priorities = new int[numberOfTriangleFaces];
            else
                    G = (byte) i_250_;
            if (i_251_ == 1)
                    face_alpha = new int[numberOfTriangleFaces];
            if (i_252_ == 1)
                    triangleTSkin = new int[numberOfTriangleFaces];
            face_color = new short[numberOfTriangleFaces];
            stream.currentOffset = i_259_;
            stream1.currentOffset = i_269_;
            stream2.currentOffset = i_270_;
            stream3.currentOffset = i_271_;
            stream4.currentOffset = i_264_;
            int i_272_ = 0;
            int i_273_ = 0;
            int i_274_ = 0;
            for (int i_275_ = 0; i_275_ < numberOfVerticeCoordinates; i_275_++) {
                    int i_276_ = stream.readUnsignedByte();
                    int i_277_ = 0;
                    if ((i_276_ & 0x1) != 0)
                    i_277_ = stream1.readSignedSmart();
                    int i_278_ = 0;
                    if ((i_276_ & 0x2) != 0)
                    i_278_ = stream2.readSignedSmart();
                    int i_279_ = 0;
                    if ((i_276_ & 0x4) != 0)
                    i_279_ = stream3.readSignedSmart();
                    verticesXCoordinate[i_275_] = i_272_ + i_277_;
                    verticesYCoordinate[i_275_] = i_273_ + i_278_;
                    verticesZCoordinate[i_275_] = i_274_ + i_279_;
                    i_272_ = verticesXCoordinate[i_275_];
                    i_273_ = verticesYCoordinate[i_275_];
                    i_274_ = verticesZCoordinate[i_275_];
                    if (i_253_ == 1)
                    vertexVSkin[i_275_] = stream4.readUnsignedByte();
            }
            stream.currentOffset = i_267_;
            stream1.currentOffset = i_263_;
            stream2.currentOffset = i_261_;
            stream3.currentOffset = i_265_;
            stream4.currentOffset = i_262_;
            for (int i_280_ = 0; i_280_ < numberOfTriangleFaces; i_280_++) {
                    face_color[i_280_] = (short) stream.readUnsignedWord();
                    if (i_249_ == 1) {
                            int i_281_ = stream1.readUnsignedByte();
                            if ((i_281_ & 0x1) == 1) {
                                    face_render_type[i_280_] = (byte) 1;
                                    has_face_type = true;
                            } else {
                                    face_render_type[i_280_] = 0;
                            }
                           
                            if ((i_281_ & 0x2) != 0) {
                                    texture_coordinates[i_280_] = (byte) (i_281_ >> 2);
                                    face_texture[i_280_] = (short) face_color[i_280_];
                                    face_color[i_280_] = 127;
                                    //face_render_type[i_280_] = 2;
                                    if (face_texture[i_280_] != -1)
                                            has_texture_type = true;
                            } else {
                                    texture_coordinates[i_280_] = -1;
                                    face_texture[i_280_] =  -1;
                            }
                    }
                    if (i_250_ == 255)
                            face_render_priorities[i_280_] = stream2.readSignedByte();
                    if (i_251_ == 1) {
                            face_alpha[i_280_] = stream3.readSignedByte();
                    }
                    if (i_252_ == 1)
                            triangleTSkin[i_280_] = stream4.readUnsignedByte();
                           
                           
            }
            stream.currentOffset = i_266_;
            stream1.currentOffset = i_260_;
            short i_282_ = 0;
            short i_283_ = 0;
            short i_284_ = 0;
            int i_285_ = 0;
            for (int face = 0; face < numberOfTriangleFaces; face++) {
                    //if(face_color[face] != 65535) {
                            int i_287_ = stream1.readUnsignedByte();
                            if (i_287_ == 1) {
                                    i_282_ = (short) (stream.readSignedSmart() + i_285_);
                                    i_285_ = i_282_;
                                    i_283_ = (short) (stream.readSignedSmart() + i_285_);
                                    i_285_ = i_283_;
                                    i_284_ = (short) (stream.readSignedSmart() + i_285_);
                                    i_285_ = i_284_;
                                    face_a[face] = i_282_;
                                    face_b[face] = i_283_;
                                    face_c[face] = i_284_;
                            }
                            if (i_287_ == 2) {
                                    i_283_ = i_284_;
                                    i_284_ = (short) (stream.readSignedSmart() + i_285_);
                                    i_285_ = i_284_;
                                    face_a[face] = i_282_;
                                    face_b[face] = i_283_;
                                    face_c[face] = i_284_;
                            }
                            if (i_287_ == 3) {
                                    i_282_ = i_284_;
                                    i_284_ = (short) (stream.readSignedSmart() + i_285_);
                                    i_285_ = i_284_;
                                    face_a[face] = i_282_;
                                    face_b[face] = i_283_;
                                    face_c[face] = i_284_;
                            }
                            if (i_287_ == 4) {
                                    short i_288_ = i_282_;
                                    i_282_ = i_283_;
                                    i_283_ = i_288_;
                                    i_284_ = (short) (stream.readSignedSmart() + i_285_);
                                    i_285_ = i_284_;
                                    face_a[face] = i_282_;
                                    face_b[face] = i_283_;
                                    face_c[face] = i_284_;
                            }
            }
            stream.currentOffset = i_268_;
            for (int face = 0; face < numberOfTexturedFaces; face++) {
                    texture_render_type[face] = 0;
                    textured_face_a[face] = (short) stream.readUnsignedWord();
                    textured_face_b[face] = (short) stream.readUnsignedWord();
                    textured_face_c[face] = (short) stream.readUnsignedWord();
            }
            if (texture_coordinates != null)
            {
                    boolean textured = false;
                    for (int face = 0; face < numberOfTriangleFaces; face++)
                    {
                            int coordinate = texture_coordinates[face] & 0xff;
                            if (coordinate != 255)
                            {
                                    if (((textured_face_a[coordinate]) == face_a[face])
                                            && ((textured_face_b[coordinate])  == face_b[face])
                                            && ((textured_face_c[coordinate]) == face_c[face]))
                                    {
                                            texture_coordinates[face] = -1;
                                    } else
                                    {
                                            textured = true;
                                    }
                            }
                    }
                    if (!textured)
                            texture_coordinates = null;
            }
            if (!has_texture_type)
                    face_texture = null;
                   
            if (!has_face_type)
                    face_render_type = null;
    }
   
    private void readOldModel(int i)
    {
            rendersWithinOneTile = false;
            ModelHeader header = modelHeader[i];
            numberOfVerticeCoordinates = header.verticeCount;
            numberOfTriangleFaces = header.triangleCount;
            numberOfTexturedFaces = header.texturedTriangleCount;
            verticesXCoordinate = new int[numberOfVerticeCoordinates];
            verticesYCoordinate = new int[numberOfVerticeCoordinates];
            verticesZCoordinate = new int[numberOfVerticeCoordinates];
            face_a = new int[numberOfTriangleFaces];
            face_b = new int[numberOfTriangleFaces];
            face_c = new int[numberOfTriangleFaces];
            textured_face_a = new int[numberOfTexturedFaces];
            textured_face_b = new int[numberOfTexturedFaces];
            textured_face_c = new int[numberOfTexturedFaces];
            if (header.vskinBasePos >= 0)
                    vertexVSkin = new int[numberOfVerticeCoordinates];
            if (header.drawTypeBasePos >= 0)
                    face_render_type = new byte[numberOfTriangleFaces];
            if (header.facePriorityBasePos >= 0)
                    face_render_priorities = new int[numberOfTriangleFaces];
            else
                    face_priority = -header.facePriorityBasePos - 1;
            if (header.alphaBasepos >= 0)
                    face_alpha = new int[numberOfTriangleFaces];
            if (header.tskinBasepos >= 0)
                    triangleTSkin = new int[numberOfTriangleFaces];
            face_color = new short[numberOfTriangleFaces];
            Stream data = new Stream(header.modelData);
            data.currentOffset = header.verticesModOffset;
            Stream stream_1 = new Stream(header.modelData);
            stream_1.currentOffset = header.verticesXOffset;
            Stream stream_2 = new Stream(header.modelData);
            stream_2.currentOffset = header.verticesYOffset;
            Stream stream_3 = new Stream(header.modelData);
            stream_3.currentOffset = header.verticesZOffset;
            Stream stream_4 = new Stream(header.modelData);
            stream_4.currentOffset = header.vskinBasePos;
            int k = 0;
            int l = 0;
            int i1 = 0;
            for (int j1 = 0; j1 < numberOfVerticeCoordinates; j1++) {
                    int k1 = data.readUnsignedByte();
                    int i2 = 0;
                    if ((k1 & 1) != 0)
                            i2 = stream_1.readSignedSmart();
                    int k2 = 0;
                    if ((k1 & 2) != 0)
                            k2 = stream_2.readSignedSmart();
                    int i3 = 0;
                    if ((k1 & 4) != 0)
                            i3 = stream_3.readSignedSmart();
                    verticesXCoordinate[j1] = k + i2;
                    verticesYCoordinate[j1] = l + k2;
                    verticesZCoordinate[j1] = i1 + i3;
                    k = verticesXCoordinate[j1];
                    l = verticesYCoordinate[j1];
                    i1 = verticesZCoordinate[j1];
                    if (vertexVSkin != null)
                            vertexVSkin[j1] = stream_4.readUnsignedByte();
            }
            data.currentOffset = header.triColourOffset;
            stream_1.currentOffset = header.drawTypeBasePos;
            stream_2.currentOffset = header.facePriorityBasePos;
            stream_3.currentOffset = header.alphaBasepos;
            stream_4.currentOffset = header.tskinBasepos;
            for (int l1 = 0; l1 < numberOfTriangleFaces; l1++) {
                    face_color[l1] = (short) data.readUnsignedWord();
                    if (face_render_type != null)
                            face_render_type[l1] = (byte) stream_1.readUnsignedByte();
                    if (face_render_priorities != null)
                            face_render_priorities[l1] = stream_2.readUnsignedByte();
                    if (face_alpha != null) {
                            face_alpha[l1] = stream_3.readUnsignedByte();
                    }
                    if (triangleTSkin != null)
                            triangleTSkin[l1] = stream_4.readUnsignedByte();
            }
            data.currentOffset = header.triVPointOffset;
            stream_1.currentOffset = header.triMeshLinkOffset;
            int j2 = 0;
            int l2 = 0;
            int j3 = 0;
            int k3 = 0;
            for (int l3 = 0; l3 < numberOfTriangleFaces; l3++) {
                    int i4 = stream_1.readUnsignedByte();
                    if (i4 == 1) {
                            j2 = data.readSignedSmart() + k3;
                            k3 = j2;
                            l2 = data.readSignedSmart() + k3;
                            k3 = l2;
                            j3 = data.readSignedSmart() + k3;
                            k3 = j3;
                            face_a[l3] = j2;
                            face_b[l3] = l2;
                            face_c[l3] = j3;
                    }
                    if (i4 == 2) {
                            j2 = j2;
                            l2 = j3;
                            j3 = data.readSignedSmart() + k3;
                            k3 = j3;
                            face_a[l3] = j2;
                            face_b[l3] = l2;
                            face_c[l3] = j3;
                    }
                    if (i4 == 3) {
                            j2 = j3;
                            l2 = l2;
                            j3 = data.readSignedSmart() + k3;
                            k3 = j3;
                            face_a[l3] = j2;
                            face_b[l3] = l2;
                            face_c[l3] = j3;
                    }
                    if (i4 == 4) {
                            int k4 = j2;
                            j2 = l2;
                            l2 = k4;
                            j3 = data.readSignedSmart() + k3;
                            k3 = j3;
                            face_a[l3] = j2;
                            face_b[l3] = l2;
                            face_c[l3] = j3;
                    }
            }
            data.currentOffset = header.textureInfoBasePos;
            for (int j4 = 0; j4 < numberOfTexturedFaces; j4++) {
                    textured_face_a[j4] = data.readUnsignedWord();
                    textured_face_b[j4] = data.readUnsignedWord();
                    textured_face_c[j4] = data.readUnsignedWord();
            }
    }
   
    public static void readFirstModelData(byte mData[], int id) {
            try {
                    if (mData == null) {
                            ModelHeader header = modelHeader[id] = new ModelHeader();
                            header.verticeCount = 0;
                            header.triangleCount = 0;
                            header.texturedTriangleCount = 0;
                            return;
                    }
                    Stream data = new Stream(mData);
                    data.currentOffset = mData.length - 18;
                    ModelHeader class21_1 = modelHeader[id] = new ModelHeader();
                    class21_1.modelData = mData;
                    class21_1.verticeCount = data.readUnsignedWord();
                    class21_1.triangleCount = data.readUnsignedWord();
                    class21_1.texturedTriangleCount = data.readUnsignedByte();
                    int k = data.readUnsignedByte();
                    int l = data.readUnsignedByte();
                    int i1 = data.readUnsignedByte();
                    int j1 = data.readUnsignedByte();
                    int k1 = data.readUnsignedByte();
                    int l1 = data.readUnsignedWord();
                    int i2 = data.readUnsignedWord();
                    int j2 = data.readUnsignedWord();
                    int k2 = data.readUnsignedWord();
                    int l2 = 0;
                    class21_1.verticesModOffset = l2;
                    l2 += class21_1.verticeCount;
                    class21_1.triMeshLinkOffset = l2;
                    l2 += class21_1.triangleCount;
                    class21_1.facePriorityBasePos = l2;
                    if (l == 255)
                            l2 += class21_1.triangleCount;
                    else
                            class21_1.facePriorityBasePos = -l - 1;
                    class21_1.tskinBasepos = l2;
                    if (j1 == 1)
                            l2 += class21_1.triangleCount;
                    else
                            class21_1.tskinBasepos = -1;
                    class21_1.drawTypeBasePos = l2;
                    if (k == 1)
                            l2 += class21_1.triangleCount;
                    else
                            class21_1.drawTypeBasePos = -1;
                    class21_1.vskinBasePos = l2;
                    if (k1 == 1)
                            l2 += class21_1.verticeCount;
                    else
                            class21_1.vskinBasePos = -1;
                    class21_1.alphaBasepos = l2;
                    if (i1 == 1)
                            l2 += class21_1.triangleCount;
                    else
                            class21_1.alphaBasepos = -1;
                    class21_1.triVPointOffset = l2;
                    l2 += k2;
                    class21_1.triColourOffset = l2;
                    l2 += class21_1.triangleCount * 2;
                    class21_1.textureInfoBasePos = l2;
                    l2 += class21_1.texturedTriangleCount * 6;
                    class21_1.verticesXOffset = l2;
                    l2 += l1;
                    class21_1.verticesYOffset = l2;
                    l2 += i2;
                    class21_1.verticesZOffset = l2;
                    l2 += j2;
            } catch (Exception e) {
                    e.printStackTrace();
            }
    }

    public static boolean newmodel[];
    
    public static void initialise(int count, OnDemandFetcherParent onDemandFetcherParent) {
            modelHeader = new ModelHeader[count];
            newmodel = new boolean[100000];
            resourceManager = onDemandFetcherParent;
    }

    public static void removeFromHeader(int j) {
            modelHeader[j] = null;
    }

    public static HDModel fetchModel(int model) {
            if (modelHeader == null)
                    return null;
                   
            ModelHeader header = Model.modelHeader[model];
            if (header == null)
            {
                    resourceManager.get(model);
                    return null;
            } else
            {
                    return new HDModel(model);
            }
    }
   
    public static boolean modelIsFetched(int i) {
            if (modelHeader == null)
                    return false;

            ModelHeader header = modelHeader[i];
            if (header == null)
            {
                    resourceManager.get(i);
                    return false;
            } else
            {
                    return true;
            }
    }

    private HDModel() {
            rendersWithinOneTile = false;
    }

    public HDModel(int i, HDModel amodel[]) {
            this(i, amodel, false);
    }
   
    public HDModel(HDModel amodel[]) {
            this(2, amodel, true);
    }
   
    public HDModel(int number_of_models, HDModel attatch[], boolean object)
    {
            try
            {
                    boolean old_format = false;
                    rendersWithinOneTile = false;
                    boolean has_render_type = false;
                    boolean has_priorities = false;
                    boolean has_alpha = false;
                    boolean has_skin = false;
                    boolean has_texture = false;
                    boolean has_coordinates = false;
                    boolean color = false;
                    numberOfVerticeCoordinates = 0;
                    numberOfTriangleFaces = 0;
                    numberOfTexturedFaces = 0;
                    face_priority = -1;
                    HDModel connect;
                    int model_index;
                    for (model_index = 0; model_index < number_of_models; model_index++)
                    {
                            connect = attatch[model_index];
                            if (connect != null)
                            {
                                    numberOfVerticeCoordinates += connect.numberOfVerticeCoordinates;
                                    numberOfTriangleFaces += connect.numberOfTriangleFaces;
                                    numberOfTexturedFaces += connect.numberOfTexturedFaces;
                                    has_render_type |= connect.face_render_type != null;
                                    has_alpha |= connect.face_alpha != null;
                                    if (connect.face_render_priorities != null)
                                    {
                                            has_priorities = true;
                                    } else
                                    {
                                            if (face_priority == -1)//-1
                                                    face_priority = connect.face_priority;
                                                   
                                            if (face_priority != connect.face_priority)
                                                    has_priorities = true;
                                    }
                                    has_coordinates = has_coordinates | connect.texture_coordinates != null;
                                    has_skin = has_skin | connect.triangleTSkin != null;
                                    has_texture = has_texture | connect.face_texture != null;
                                    color = color | connect.face_color != null;
                            }
                    }
                    if(color)
                            face_color = new short[numberOfTriangleFaces];
                           
                    verticesXCoordinate = new int[numberOfVerticeCoordinates];
                    verticesYCoordinate = new int[numberOfVerticeCoordinates];
                    verticesZCoordinate = new int[numberOfVerticeCoordinates];
                    vertexVSkin = new int[numberOfVerticeCoordinates];
                    face_a = new int[numberOfTriangleFaces];
                    face_b = new int[numberOfTriangleFaces];
                    face_c = new int[numberOfTriangleFaces];
                   
                    if (has_render_type)
                            face_render_type = new byte[numberOfTriangleFaces];
                   
                    if (has_skin)
                            triangleTSkin = new int[numberOfTriangleFaces];

                    if(object)
                    {
                            face_shade_a = new int[numberOfTriangleFaces];
                            face_shade_b = new int[numberOfTriangleFaces];
                            face_shade_c = new int[numberOfTriangleFaces];
                    }
                   
                    if(numberOfTexturedFaces > 0)
                    {
                            textured_face_a = new int[numberOfTexturedFaces];
                            textured_face_b = new int[numberOfTexturedFaces];
                            textured_face_c = new int[numberOfTexturedFaces];
                            texture_render_type = new byte[numberOfTexturedFaces];
                    }
                   
                    if(has_coordinates)
                            texture_coordinates = new byte[numberOfTriangleFaces];
                           
                    if(has_texture)
                            face_texture = new short[numberOfTriangleFaces];
                           
                    if (has_alpha)
                            face_alpha = new int[numberOfTriangleFaces];
                   
                    if (has_priorities)
                            face_render_priorities = new int[numberOfTriangleFaces];
                   
                    numberOfVerticeCoordinates = 0;
                    numberOfTriangleFaces = 0;
                    numberOfTexturedFaces = 0;
                    int[] offsets = null;
                    int texture_face = 0;
                    for(model_index = 0; model_index < number_of_models; model_index++)
                    {
                            connect = attatch[model_index];
                            if (connect != null)
                            {
                                    int vertex_coord = -1;
                                    if(offsets != null)
                                    {
                                            vertex_coord = offsets[number_of_models] = numberOfVerticeCoordinates;
                                            for (int l1 = 0; l1 < connect.numberOfVerticeCoordinates; l1++)
                                            {
                                                    verticesXCoordinate[numberOfVerticeCoordinates] = connect.verticesXCoordinate[l1];
                                                    verticesYCoordinate[numberOfVerticeCoordinates] = connect.verticesYCoordinate[l1];
                                                    verticesZCoordinate[numberOfVerticeCoordinates] = connect.verticesZCoordinate[l1];
                                                    vertexVSkin[numberOfVerticeCoordinates] = connect.vertexVSkin != null ? connect.vertexVSkin[l1] : -1;
                                                    numberOfVerticeCoordinates++;
                                            }
                                    }
                                    for (int j1 = 0; j1 < connect.numberOfTriangleFaces; j1++)
                                    {
                                            if (has_render_type)
                                            {
                                                    if(old_format)
                                                    {
                                                            if (connect.face_render_type == null)
                                                            {
                                                                    face_render_type[numberOfTriangleFaces] = 0;
                                                            } else
                                                            {
                                                                    int type = connect.face_render_type[j1];
                                                                    if ((type & 2) == 2)
                                                                            type += texture_face << 2;
                                                                    face_render_type[numberOfTriangleFaces] = (byte) type;
                                                            }
                                                    } else
                                                    {
                                                            if(connect.face_render_type != null)
                                                            {
                                                                    face_render_type[numberOfTriangleFaces] = connect.face_render_type[j1];
                                                            } else
                                                            {
                                                                    old_format = true;
                                                            }
                                                    }
                                           
                                            }
                                            if (has_priorities && connect.face_render_priorities != null)
                                                    face_render_priorities[numberOfTriangleFaces] = connect.face_render_priorities[j1];
                                            else
                                                    face_render_priorities[numberOfTriangleFaces] = connect.face_priority;
                                                   
                                           
                                            if (has_alpha && connect.face_alpha != null)
                                                    face_alpha[numberOfTriangleFaces] = connect.face_alpha[j1];
                                           
                                            if (has_texture)
                                            {
                                                    if (connect.face_texture != null  && connect.face_texture[j1] != -1)
                                                    {
                                                            face_texture[numberOfTriangleFaces] = connect.face_texture[j1];
                                                            display_model_specific_texture = true;
                                                            //force_texture = true;
                                                    } else
                                                    {
                                                            face_texture[numberOfTriangleFaces] = (short) -1;
                                                           
                                                    }
                                            }
                                           
                                            if (has_alpha && connect.triangleTSkin != null)
                                                    triangleTSkin[numberOfTriangleFaces] = connect.triangleTSkin[j1];
                                                           
                                            if(object)
                                            {
                                                    face_shade_a[numberOfTriangleFaces] = connect.face_shade_a[j1];
                                                    face_shade_b[numberOfTriangleFaces] = connect.face_shade_b[j1];
                                                    face_shade_c[numberOfTriangleFaces] = connect.face_shade_c[j1];
                                            }
                                            if(vertex_coord != -1)
                                            {
                                                    face_a[numberOfTriangleFaces] = (short) (connect.face_a[j1]) + vertex_coord;
                                                    face_b[numberOfTriangleFaces] = (short) (connect.face_b[j1]) + vertex_coord;
                                                    face_c[numberOfTriangleFaces] = (short) (connect.face_c[j1]) + vertex_coord;
                                            } else
                                            {
                                                    face_a[numberOfTriangleFaces] = (short) (method465(connect, connect.face_a[j1]));
                                                    face_b[numberOfTriangleFaces] = (short) (method465(connect, connect.face_b[j1]));
                                                    face_c[numberOfTriangleFaces] = (short) (method465(connect, connect.face_c[j1]));
                                            }
                                            if(color)
                                            {
                                                    if (connect.face_color != null)
                                                    {
                                                            face_color[numberOfTriangleFaces] = connect.face_color[j1];
                                                    } else
                                                    {
                                                            face_color[numberOfTriangleFaces] = (short) 0;
                                                    }
                                            }
                                            numberOfTriangleFaces++;
                                    }
                            }
                    }
                    int face = 0;
                    for(model_index = 0; model_index < number_of_models; model_index++)
                    {
                            connect = attatch[model_index];
                            if(connect != null)
                            {
                                    if(has_coordinates)
                                    {
                                            for(int mapped_pointers  = 0; mapped_pointers  < connect.numberOfTriangleFaces; mapped_pointers ++) {
                                            	if(numberOfTriangleFaces >= texture_coordinates.length)
                                            		continue;
                                            	if(connect.texture_coordinates != null && mapped_pointers >= connect.texture_coordinates.length)
                                            		continue;
                                                    texture_coordinates[numberOfTriangleFaces] = 
                                                    		(byte) (connect.texture_coordinates == null || connect.texture_coordinates[mapped_pointers ] == -1 
                                                    		? -1 :
                                                            (connect.texture_coordinates[mapped_pointers ] & 0xff) + numberOfTexturedFaces);
                                            }
                                   
                                    }
                                    int vertex_coord = offsets != null ? offsets[model_index] : -1;//short vertex_coord = (short) (1 << l1);
                                    for (int texture_index = 0; texture_index < connect.numberOfTexturedFaces; texture_index++)
                                    {
                                            byte opcode = (texture_render_type[numberOfTexturedFaces] = connect.texture_render_type[texture_index]);
                                            if (opcode == 0)
                                            {
                                                    if(vertex_coord != -1)
                                                    {
                                                            textured_face_a[numberOfTexturedFaces] = (short) method465(connect,
                                                                    (connect.textured_face_a[texture_index]) + vertex_coord);
                                                            textured_face_b[numberOfTexturedFaces] = (short) method465(connect,
                                                                    (connect.textured_face_b[texture_index]) + vertex_coord);
                                                            textured_face_c[numberOfTexturedFaces] = (short) method465(connect,
                                                                    (connect.textured_face_c[texture_index]) + vertex_coord);
                                                    } else
                                                    {
                                                            textured_face_a[numberOfTexturedFaces] = (short) method465(connect,
                                                                    connect.textured_face_a[texture_index]);
                                                            textured_face_b[numberOfTexturedFaces] = (short) method465(connect,
                                                                            connect.textured_face_b[texture_index]);
                                                            textured_face_c[numberOfTexturedFaces] = (short) method465(connect,
                                                                            connect.textured_face_c[texture_index]);
                                                    }
                                            }
                                            if(opcode >= 1 && opcode <= 3)
                                            {
                                                    if(vertex_coord != -1)
                                                    {
                                                            textured_face_a[numberOfTexturedFaces] = (short) ((connect.textured_face_a[texture_index]) + vertex_coord);
                                                            textured_face_b[numberOfTexturedFaces] = (short) ((connect.textured_face_b[texture_index]) + vertex_coord);
                                                            textured_face_c[numberOfTexturedFaces] = (short) ((connect.textured_face_c[texture_index]) + vertex_coord);
                                                    } else
                                                    {
                                                            textured_face_a[numberOfTexturedFaces] = connect.textured_face_a[texture_index];
                                                            textured_face_b[numberOfTexturedFaces] = connect.textured_face_b[texture_index];
                                                            textured_face_c[numberOfTexturedFaces] = connect.textured_face_c[texture_index];
                                                    }
                                            }
                                            if(opcode == 2)
                                            {
                                           
                                            }
                                            numberOfTexturedFaces++;
                                    }
                                    if(old_format)
                                            texture_face += connect.numberOfTexturedFaces;
                            }
                    }
                    if(object)
                    {
                            calculateDiagonals();
                    }
            } catch (Exception e)
            {
                    e.printStackTrace();
            }
    }
   
    public HDModel(boolean flag, boolean flag1, boolean flag2, HDModel model)
    {
            this(flag,  flag1,  flag2, false, model);
    }
   
    public HDModel(boolean flag, boolean flag1, boolean flag2, boolean texture, HDModel model) {//objects (trees/bushes/walls/roofs/etc) //Graphcis //NPCs
            rendersWithinOneTile = false;
            numberOfVerticeCoordinates = model.numberOfVerticeCoordinates;
            numberOfTriangleFaces = model.numberOfTriangleFaces;
            numberOfTexturedFaces = model.numberOfTexturedFaces;
            if (flag2) {
                    verticesXCoordinate = model.verticesXCoordinate;
                    verticesYCoordinate = model.verticesYCoordinate;
                    verticesZCoordinate = model.verticesZCoordinate;
            } else {
                    verticesXCoordinate = new int[numberOfVerticeCoordinates];
                    verticesYCoordinate = new int[numberOfVerticeCoordinates];
                    verticesZCoordinate = new int[numberOfVerticeCoordinates];
                    for (int j = 0; j < numberOfVerticeCoordinates; j++) {
                            verticesXCoordinate[j] = model.verticesXCoordinate[j];
                            verticesYCoordinate[j] = model.verticesYCoordinate[j];
                            verticesZCoordinate[j] = model.verticesZCoordinate[j];
                    }

            }
            if (flag) {
                    face_color = model.face_color;
            } else {
                    face_color = new short[numberOfTriangleFaces];
                    if(model.face_color != null)
                            for (int k = 0; k != numberOfTriangleFaces; k++)
                                    face_color[k] = model.face_color[k];
            }
           
            if (flag1) {
                    face_alpha = model.face_alpha;
            } else {
                    face_alpha = new int[numberOfTriangleFaces];
                    if (model.face_alpha != null)
                    {
                            for (int i1 = 0; i1 < numberOfTriangleFaces; i1++)
                                    face_alpha[i1] = model.face_alpha[i1];
                    }
            }
            vertexVSkin = model.vertexVSkin;
            triangleTSkin = model.triangleTSkin;
            if(numberOfTexturedFaces > 0) {
                    textured_face_a = model.textured_face_a;
                    textured_face_b = model.textured_face_b;
                    textured_face_c = model.textured_face_c;
                    face_texture = model.face_texture;
                    display_model_specific_texture = texture;
                    texture_coordinates = model.texture_coordinates;
                    texture_render_type = model.texture_render_type;
            }
            face_a = model.face_a;
            face_b = model.face_b;
            face_c = model.face_c;
            face_priority = model.face_priority;
            face_render_priorities = model.face_render_priorities;
            face_render_type = model.face_render_type;
    }
   
    public HDModel(boolean flag, boolean flag1, HDModel model) {//objects - adjustToTerrain, nonFlatShading (hedges, terrain objects?)
            rendersWithinOneTile = false;
            anInt1620++;
            numberOfVerticeCoordinates = model.numberOfVerticeCoordinates;
            numberOfTriangleFaces = model.numberOfTriangleFaces;
            numberOfTexturedFaces = model.numberOfTexturedFaces;
            if (flag) {
                    verticesYCoordinate = new int[numberOfVerticeCoordinates];
                    for (int j = 0; j < numberOfVerticeCoordinates; j++)
                            verticesYCoordinate[j] = model.verticesYCoordinate[j];

            } else {
                    verticesYCoordinate = model.verticesYCoordinate;
            }

            if(numberOfTexturedFaces > 0) {
                    textured_face_a = model.textured_face_a;
                    textured_face_b = model.textured_face_b;
                    textured_face_c = model.textured_face_c;
                    face_texture = model.face_texture;
                    texture_coordinates = model.texture_coordinates;
                    texture_render_type = model.texture_render_type;
            }
           
            if (flag1) {
                    face_shade_a = new int[numberOfTriangleFaces];
                    face_shade_b = new int[numberOfTriangleFaces];
                    face_shade_c = new int[numberOfTriangleFaces];
                    for (int k = 0; k < numberOfTriangleFaces; k++) {
                            face_shade_a[k] = model.face_shade_a[k];
                            face_shade_b[k] = model.face_shade_b[k];
                            face_shade_c[k] = model.face_shade_c[k];
                    }

                    face_render_type = new byte[numberOfTriangleFaces];
                    if (model.face_render_type == null) {
                            for (int l = 0; l < numberOfTriangleFaces; l++)
                                    face_render_type[l] = 0;

                    } else {
                            for (int i1 = 0; i1 < numberOfTriangleFaces; i1++)
                                    face_render_type[i1] = model.face_render_type[i1];

                    }
                    super.vertexNormals = new VertexNormal[numberOfVerticeCoordinates];
                    for (int j1 = 0; j1 < numberOfVerticeCoordinates; j1++) {
                            VertexNormal class33 = super.vertexNormals[j1] = new VertexNormal();
                            VertexNormal class33_1 = model.vertexNormals[j1];
                            class33.anInt602 = class33_1.anInt602;
                            class33.anInt603 = class33_1.anInt603;
                            class33.anInt604 = class33_1.anInt604;
                            class33.anInt605 = class33_1.anInt605;
                    }

                    vertexNormalOffset = model.vertexNormalOffset;
            } else {
                    face_shade_a = model.face_shade_a;
                    face_shade_b = model.face_shade_b;
                    face_shade_c = model.face_shade_c;
                    face_render_type = model.face_render_type;
            }
            verticesXCoordinate = model.verticesXCoordinate;
            verticesZCoordinate = model.verticesZCoordinate;
            face_color = model.face_color;
            face_render_priorities = model.face_render_priorities;
            face_priority = model.face_priority;
            face_alpha = model.face_alpha;
            face_a = model.face_a;
            face_b = model.face_b;
            face_c = model.face_c;
            super.modelHeight = model.modelHeight;
            anInt1650 = model.anInt1650;
            anInt1653 = model.anInt1653;
            anInt1652 = model.anInt1652;
            anInt1646 = model.anInt1646;
            anInt1648 = model.anInt1648;
            anInt1649 = model.anInt1649;
            anInt1647 = model.anInt1647;
    }
   
    public void method464(HDModel model, boolean flag) {
            method464(model, flag, true, false, false);
    }
   
    public void method464(HDModel model, boolean flag, boolean texture) {
            method464(model, flag, texture, false, false);
    }
   
    public void method464(HDModel model, boolean flag, boolean texture, boolean player) {
            method464(model, flag, texture, player, false);
    }
   
    public void method464(HDModel model, boolean flag, boolean texture, boolean player_texture, boolean npc) {//Worn items / Npcs
            numberOfVerticeCoordinates = model.numberOfVerticeCoordinates;
            numberOfTriangleFaces = model.numberOfTriangleFaces;
            numberOfTexturedFaces = model.numberOfTexturedFaces;
            if (anIntArray1622.length < numberOfVerticeCoordinates) {
                    anIntArray1622 = new int[
                            Math.max(anIntArray1622.length * 2, numberOfTriangleFaces)
                            //numberOfVerticeCoordinates + 10000
                    ];
                    anIntArray1623 = new int[
                            Math.max(anIntArray1623.length * 2, numberOfTriangleFaces)
                            //numberOfVerticeCoordinates + 10000
                    ];
                    anIntArray1624 = new int[
                            Math.max(anIntArray1624.length * 2, numberOfTriangleFaces)
                            //numberOfVerticeCoordinates + 10000
                    ];
            }
            verticesXCoordinate = anIntArray1622;
            verticesYCoordinate = anIntArray1623;
            verticesZCoordinate = anIntArray1624;
            for (int k = 0; k < numberOfVerticeCoordinates; k++) {
                    if(model.verticesXCoordinate != null)
                            verticesXCoordinate[k] = model.verticesXCoordinate[k];
                    if(model.verticesYCoordinate != null)
                            verticesYCoordinate[k] = model.verticesYCoordinate[k];
                    if(model.verticesZCoordinate != null)
                            verticesZCoordinate[k] = model.verticesZCoordinate[k];
            }
            if(numberOfTexturedFaces > 0) {
                    textured_face_a = model.textured_face_a;
                    textured_face_b = model.textured_face_b;
                    textured_face_c = model.textured_face_c;
                   
                    if(texture)
                    {
                            face_texture = model.face_texture;
                    } else
                    {
                            face_texture = new short[numberOfTriangleFaces];
                            if(model.face_texture != null)
                                    for(int l = 0; l < numberOfTriangleFaces; l++)
                                            face_texture[l] = -1;
                    }
                    force_texture = npc;//npc;
                    display_model_specific_texture = texture;
                    texture_coordinates = model.texture_coordinates;
                    texture_render_type = model.texture_render_type;
                   
            }
            if (flag) {
                    face_alpha = model.face_alpha;
            } else {
                    if (anIntArray1625.length < numberOfTriangleFaces)
                            anIntArray1625 = new int[
                                    Math.max(anIntArray1625.length * 2, numberOfTriangleFaces)
                                    //numberOfTriangleFaces + 100
                            ];
                    face_alpha = anIntArray1625;
                    if (model.face_alpha == null) {
                            for (int l = 0; l < numberOfTriangleFaces; l++)
                                    face_alpha[l] = 0;

                    } else {
                            for (int i1 = 0; i1 != numberOfTriangleFaces; i1++)
                                    face_alpha[i1] = model.face_alpha[i1];

                    }
            }
            face_render_type = model.face_render_type;
            if(flag) {
                    face_color = model.face_color;
            } else {
                    if(anIntArray1626.length < numberOfTriangleFaces)
                            anIntArray1626 = new short[
                                    Math.max(anIntArray1625.length * 2, numberOfTriangleFaces)
                            ];

                    face_color = anIntArray1626;
                    if(model.face_color != null)
                            for(int l = 0; l < numberOfTriangleFaces; l++)
                                    face_color[l] = model.face_color[l];
            }
            face_render_priorities = model.face_render_priorities;
            face_priority = model.face_priority;
            triangleSkin = model.triangleSkin;
            vertexSkin = model.vertexSkin;
            face_a = model.face_a;
            face_b = model.face_b;
            face_c = model.face_c;
            face_shade_a = model.face_shade_a;
            face_shade_b = model.face_shade_b;
            face_shade_c = model.face_shade_c;
    }

    private final int method465(HDModel model, int i) {
            int j = -1;
            int k = model.verticesXCoordinate[i];
            int l = model.verticesYCoordinate[i];
            int i1 = model.verticesZCoordinate[i];
            for (int j1 = 0; j1 < numberOfVerticeCoordinates; j1++) {
                    if (k != verticesXCoordinate[j1] || l != verticesYCoordinate[j1] || i1 != verticesZCoordinate[j1])
                            continue;
                    j = j1;
                    break;
            }

            if (j == -1) {
                    verticesXCoordinate[numberOfVerticeCoordinates] = k;
                    verticesYCoordinate[numberOfVerticeCoordinates] = l;
                    verticesZCoordinate[numberOfVerticeCoordinates] = i1;
                    if (model.vertexVSkin != null)
                            vertexVSkin[numberOfVerticeCoordinates] = model.vertexVSkin[i];
                    j = numberOfVerticeCoordinates++;
            }
            return j;
    }

    public void calculateDiagonals() {//calculateDiagonals
            super.modelHeight = 0;
            anInt1650 = 0;
            anInt1651 = 0;//maxX
            for (int i = 0; i < numberOfVerticeCoordinates; i++) {
                    int j = verticesXCoordinate[i];//verticesXCoordinate
                    int k = verticesYCoordinate[i];//verticesYCoordinate
                    int l = verticesZCoordinate[i];//verticesZCoordinate
                    if (-k > super.modelHeight)
                            super.modelHeight = -k;
                    if (k > anInt1651)
                            anInt1651 = k;
                    int i1 = j * j + l * l;
                    if (i1 > anInt1650)
                            anInt1650 = i1;
            }
            anInt1650 = (int) (Math.sqrt(anInt1650) + 0.98999999999999999D);
            anInt1653 = (int) (Math.sqrt(anInt1650 * anInt1650 + super.modelHeight
                            * super.modelHeight) + 0.98999999999999999D);
            anInt1652 = anInt1653
            + (int) (Math.sqrt(anInt1650 * anInt1650 + anInt1651
                            * anInt1651) + 0.98999999999999999D);
    }

    public void normalise() {//normalize
            super.modelHeight = 0;
            anInt1651 = 0;
            for (int i = 0; i < numberOfVerticeCoordinates; i++) {
                    int j = verticesYCoordinate[i];
                    if (-j > super.modelHeight)
                            super.modelHeight = -j;
                    if (j > anInt1651)
                            anInt1651 = j;
            }
            anInt1653 = (int) (Math.sqrt(anInt1650 * anInt1650 + super.modelHeight
                            * super.modelHeight) + 0.98999999999999999D);
            anInt1652 = anInt1653
            + (int) (Math.sqrt(anInt1650 * anInt1650 + anInt1651
                            * anInt1651) + 0.98999999999999999D);
    }

    public void calcDiagonalsAndStats(int i) {//calcDiagonalsAndStats
            super.modelHeight = 0;
            anInt1650 = 0;
            anInt1651 = 0;
            anInt1646 = 0xf423f;
            anInt1647 = 0xfff0bdc1;
            anInt1648 = 0xfffe7961;
            anInt1649 = 0x1869f;
            for (int j = 0; j < numberOfVerticeCoordinates; j++) {
                    int k = verticesXCoordinate[j];
                    int l = verticesYCoordinate[j];
                    int i1 = verticesZCoordinate[j];
                    if (k < anInt1646)
                            anInt1646 = k;
                    if (k > anInt1647)
                            anInt1647 = k;
                    if (i1 < anInt1649)
                            anInt1649 = i1;
                    if (i1 > anInt1648)
                            anInt1648 = i1;
                    if (-l > super.modelHeight)
                            super.modelHeight = -l;
                    if (l > anInt1651)
                            anInt1651 = l;
                    int j1 = k * k + i1 * i1;
                    if (j1 > anInt1650)
                            anInt1650 = j1;
            }

            anInt1650 = (int) Math.sqrt(anInt1650);
            anInt1653 = (int) Math.sqrt(anInt1650 * anInt1650 + super.modelHeight
                            * super.modelHeight);
            if (i != 21073) {
                    return;
            } else {
                    anInt1652 = anInt1653
                    + (int) Math.sqrt(anInt1650 * anInt1650 + anInt1651
                                    * anInt1651);
                    return;
            }
    }

    public void createBones() {
            if (vertexVSkin != null) {
                    int ai[] = new int[256];
                    int j = 0;
                    for (int l = 0; l < numberOfVerticeCoordinates; l++) {
                            int j1 = vertexVSkin[l];
                            ai[j1]++;
                            if (j1 > j)
                                    j = j1;
                    }

                    vertexSkin = new int[j + 1][];
                    for (int k1 = 0; k1 <= j; k1++) {
                            vertexSkin[k1] = new int[ai[k1]];
                            ai[k1] = 0;
                    }

                    for (int j2 = 0; j2 < numberOfVerticeCoordinates; j2++) {
                            int l2 = vertexVSkin[j2];
                            vertexSkin[l2][ai[l2]++] = j2;
                    }

                    vertexVSkin = null;
            }
            if (triangleTSkin != null) {
                    int ai1[] = new int[256];
                    int k = 0;
                    for (int i1 = 0; i1 < numberOfTriangleFaces; i1++) {
                            int l1 = triangleTSkin[i1];
                            ai1[l1]++;
                            if (l1 > k)
                                    k = l1;
                    }

                    triangleSkin = new int[k + 1][];
                    for (int i2 = 0; i2 <= k; i2++) {
                            triangleSkin[i2] = new int[ai1[i2]];
                            ai1[i2] = 0;
                    }

                    for (int k2 = 0; k2 < numberOfTriangleFaces; k2++) {
                            int i3 = triangleTSkin[k2];
                            triangleSkin[i3][ai1[i3]++] = k2;
                    }

                    triangleTSkin = null;
            }
    }

    public void applyTransform(int frame, int nextFrame, int end, int cycle) {

		if (vertexSkin != null && frame != -1) {
			FrameReader currentAnimation = FrameReader.forID(frame);
    		SkinList list1 = currentAnimation.mySkinList;
    		anInt1681 = 0;
    		anInt1682 = 0;
    		anInt1683 = 0;
    	    FrameReader nextAnimation = null;
    	    SkinList list2 = null;
    	    if (nextFrame != -1) {
    			nextAnimation = FrameReader.forID(nextFrame);
    			if (nextAnimation.mySkinList != list1)
    				nextAnimation = null;
    			list2 = nextAnimation.mySkinList;
    	    }
    	    if(nextAnimation == null || list2 == null) {
	    		for (int step = 0; step < currentAnimation.stepCount; step++) {
	    			int i_264_ = currentAnimation.opCodeLinkTable[step];
	    			transformStep(list1.opcodes[i_264_], list1.skinList[i_264_], currentAnimation.xOffset[step], currentAnimation.yOffset[step], currentAnimation.zOffset[step]);
	    		
	    		}
    	    } else {
    	    	for (int i1 = 0; i1 < currentAnimation.stepCount; i1++) {
    	    		int n1 = currentAnimation.opCodeLinkTable[i1];
    	    		int opcode = list1.opcodes[n1];
    	    		int[] skin = list1.skinList[n1];
    	    		int x = currentAnimation.xOffset[i1];
    	    		int y = currentAnimation.yOffset[i1];
    	    		int z = currentAnimation.zOffset[i1];
    	    		boolean found = false;
    	    		for (int i2 = 0; i2 < nextAnimation.stepCount; i2++) {
    	    			int n2 = nextAnimation.opCodeLinkTable[i2];
    	    			if (list2.skinList[n2].equals(skin)) {
    	    				if (opcode != 2) {
    	    					x += (nextAnimation.xOffset[i2] - x) * cycle / end;
    	    					y += (nextAnimation.yOffset[i2] - y) * cycle / end;
    	    					z += (nextAnimation.zOffset[i2] - z) * cycle / end;
    	    				} else {
    	    					x &= 0xff;
    	    					y &= 0xff;
    	    					z &= 0xff;
    	    					int dx = nextAnimation.xOffset[i2] - x & 0xff;
    	    					int dy = nextAnimation.yOffset[i2] - y & 0xff;
    	    					int dz = nextAnimation.zOffset[i2] - z & 0xff;
    	    					if (dx >= 128) {
    	    						dx -= 256;
    	    					}
    	    					if (dy >= 128) {
    	    						dy -= 256;
    	    					}
    	    					if (dz >= 128) {
    	    						dz -= 256;
    	    					}
    	    					x = x + dx * cycle / end & 0xff;
    	    					y = y + dy * cycle / end & 0xff;
    	    					z = z + dz * cycle / end & 0xff;
							}
							found = true;
							break;
    	    			}
    	    		}
    	    		if (!found) {
    	    			if (opcode != 3 && opcode != 2) {
    	    				x = x * (end - cycle) / end;
        	    			y = y * (end - cycle) / end;
        	    			z = z * (end - cycle) / end;
    	    			} else if (opcode == 3) {
    	    				x = (x * (end - cycle) + (cycle << 7)) / end;
    	    				y = (y * (end - cycle) + (cycle << 7)) / end;
    	    				z = (z * (end - cycle) + (cycle << 7)) / end;
    	    			} else {
    	    				x &= 0xff;
	    					y &= 0xff;
	    					z &= 0xff;
	    					int dx = -x & 0xff;
	    					int dy = -y & 0xff;
	    					int dz = -z & 0xff;
	    					if (dx >= 128) {
	    						dx -= 256;
	    					}
	    					if (dy >= 128) {
	    						dy -= 256;
	    					}
	    					if (dz >= 128) {
	    						dz -= 256;
	    					}
	    					x = x + dx * cycle / end & 0xff;
	    					y = y + dy * cycle / end & 0xff;
	    					z = z + dz * cycle / end & 0xff;
    	    			}
    	    		}
    	    		transformStep(opcode, skin, x, y, z);
    	    	}
    	    }
		}
	}
    
    public void applyTransform(int i) {
            if (vertexSkin == null)
                    return;
            if (i == -1)
                    return;
            FrameReader class36 = FrameReader.forID(i);
            if (class36 == null)
                    return;
            SkinList class18 = class36.mySkinList;
            anInt1681 = 0;
            anInt1682 = 0;
            anInt1683 = 0;
            for (int k = 0; k < class36.stepCount; k++) {
                    int l = class36.opCodeLinkTable[k];
                    transformStep(class18.opcodes[l], class18.skinList[l],
                                    class36.xOffset[k], class36.yOffset[k],
                                    class36.zOffset[k]);
            }

    }

    public void mixTransform(int ai[], int j, int k) {
            if (k == -1)
                    return;
            if (ai == null || j == -1) {
                    applyTransform(k);
                    return;
            }
            FrameReader class36 = FrameReader.forID(k);
            if (class36 == null)
                    return;
            FrameReader class36_1 = FrameReader.forID(j);
            if (class36_1 == null) {
                    applyTransform(k);
                    return;
            }
            SkinList class18 = class36.mySkinList;
            anInt1681 = 0;
            anInt1682 = 0;
            anInt1683 = 0;
            int l = 0;
            int i1 = ai[l++];
            for (int j1 = 0; j1 < class36.stepCount; j1++) {
                    int k1;
                    for (k1 = class36.opCodeLinkTable[j1]; k1 > i1; i1 = ai[l++])
                            ;
                    if (k1 != i1 || class18.opcodes[k1] == 0)
                            transformStep(class18.opcodes[k1],
                                            class18.skinList[k1],
                                            class36.xOffset[j1], class36.yOffset[j1],
                                            class36.zOffset[j1]);
            }

            anInt1681 = 0;
            anInt1682 = 0;
            anInt1683 = 0;
            l = 0;
            i1 = ai[l++];
            for (int l1 = 0; l1 < class36_1.stepCount; l1++) {
                    int i2;
                    for (i2 = class36_1.opCodeLinkTable[l1]; i2 > i1; i1 = ai[l++])
                            ;
                    if (i2 == i1 || class18.opcodes[i2] == 0)
                            transformStep(class18.opcodes[i2],
                                            class18.skinList[i2],
                                            class36_1.xOffset[l1],
                                            class36_1.yOffset[l1],
                                            class36_1.zOffset[l1]);
            }

    }

    private void transformStep(int i, int ai[], int j, int k, int l) {

            int i1 = ai.length;
            if (i == 0) {
                    int j1 = 0;
                    anInt1681 = 0;
                    anInt1682 = 0;
                    anInt1683 = 0;
                    for (int k2 = 0; k2 < i1; k2++) {
                            int l3 = ai[k2];
                            if (l3 < vertexSkin.length) {
                                    int ai5[] = vertexSkin[l3];
                                    for (int i5 = 0; i5 < ai5.length; i5++) {
                                            int j6 = ai5[i5];
                                            anInt1681 += verticesXCoordinate[j6];
                                            anInt1682 += verticesYCoordinate[j6];
                                            anInt1683 += verticesZCoordinate[j6];
                                            j1++;
                                    }

                            }
                    }

                    if (j1 > 0) {
                            anInt1681 = anInt1681 / j1 + j;
                            anInt1682 = anInt1682 / j1 + k;
                            anInt1683 = anInt1683 / j1 + l;
                            return;
                    } else {
                            anInt1681 = j;
                            anInt1682 = k;
                            anInt1683 = l;
                            return;
                    }
            }
            if (i == 1) {
                    for (int k1 = 0; k1 < i1; k1++) {
                            int l2 = ai[k1];
                            if (l2 < vertexSkin.length) {
                                    int ai1[] = vertexSkin[l2];
                                    for (int i4 = 0; i4 < ai1.length; i4++) {
                                            int j5 = ai1[i4];
                                            verticesXCoordinate[j5] += j;
                                            verticesYCoordinate[j5] += k;
                                            verticesZCoordinate[j5] += l;
                                    }

                            }
                    }

                    return;
            }
            if (i == 2) {
                    for (int l1 = 0; l1 < i1; l1++) {
                            int i3 = ai[l1];
                            if (i3 < vertexSkin.length) {
                                    int ai2[] = vertexSkin[i3];
                                    for (int j4 = 0; j4 < ai2.length; j4++) {
                                            int k5 = ai2[j4];
                                            verticesXCoordinate[k5] -= anInt1681;
                                            verticesYCoordinate[k5] -= anInt1682;
                                            verticesZCoordinate[k5] -= anInt1683;
                                            int k6 = (j & 0xff) * 8;
                                            int l6 = (k & 0xff) * 8;
                                            int i7 = (l & 0xff) * 8;
                                            if (i7 != 0) {
                                                    int j7 = SINE[i7];
                                                    int i8 = COSINE[i7];
                                                    int l8 = verticesYCoordinate[k5] * j7 + verticesXCoordinate[k5] * i8 >> 16;
                                    verticesYCoordinate[k5] = verticesYCoordinate[k5] * i8
                                    - verticesXCoordinate[k5] * j7 >> 16;
                    verticesXCoordinate[k5] = l8;
                                            }
                                            if (k6 != 0) {
                                                    int k7 = SINE[k6];
                                                    int j8 = COSINE[k6];
                                                    int i9 = verticesYCoordinate[k5] * j8 - verticesZCoordinate[k5] * k7 >> 16;
                                                    verticesZCoordinate[k5] = verticesYCoordinate[k5] * k7 + verticesZCoordinate[k5] * j8 >> 16;
                                                    verticesYCoordinate[k5] = i9;
                                            }
                                            if (l6 != 0) {
                                                    int l7 = SINE[l6];
                                                    int k8 = COSINE[l6];
                                                    int j9 = verticesZCoordinate[k5] * l7 + verticesXCoordinate[k5] * k8 >> 16;
                                                    verticesZCoordinate[k5] = verticesZCoordinate[k5] * k8 - verticesXCoordinate[k5] * l7 >> 16;
                                                    verticesXCoordinate[k5] = j9;
                                            }
                                            verticesXCoordinate[k5] += anInt1681;
                                            verticesYCoordinate[k5] += anInt1682;
                                            verticesZCoordinate[k5] += anInt1683;
                                    }

                            }
                    }
                    return;
            }
            if (i == 3) {
                    for (int i2 = 0; i2 < i1; i2++) {
                            int j3 = ai[i2];
                            if (j3 < vertexSkin.length) {
                                    int ai3[] = vertexSkin[j3];
                                    for (int k4 = 0; k4 < ai3.length; k4++) {
                                            int l5 = ai3[k4];
                                            verticesXCoordinate[l5] -= anInt1681;
                                            verticesYCoordinate[l5] -= anInt1682;
                                            verticesZCoordinate[l5] -= anInt1683;
                                            verticesXCoordinate[l5] = (verticesXCoordinate[l5] * j) / 128;
                                            verticesYCoordinate[l5] = (verticesYCoordinate[l5] * k) / 128;
                                            verticesZCoordinate[l5] = (verticesZCoordinate[l5] * l) / 128;
                                            verticesXCoordinate[l5] += anInt1681;
                                            verticesYCoordinate[l5] += anInt1682;
                                            verticesZCoordinate[l5] += anInt1683;
                                    }
                            }
                    }
                    return;
            }
            if (i == 5 && triangleSkin != null && face_alpha != null) {
                    for (int j2 = 0; j2 < i1; j2++) {
                            int k3 = ai[j2];
                            if (k3 < triangleSkin.length) {
                                    int ai4[] = triangleSkin[k3];
                                    for (int l4 = 0; l4 < ai4.length; l4++) {
                                            int i6 = ai4[l4];
                                            face_alpha[i6] += j * 8;
                                            if (face_alpha[i6] < 0)
                                                    face_alpha[i6] = 0;
                                            if (face_alpha[i6] > 255)
                                                    face_alpha[i6] = 255;
                                    }
                            }
                    }
            }
    }

    public void rotateBy90() {
            for (int j = 0; j < numberOfVerticeCoordinates; j++) {
                    int k = verticesXCoordinate[j];
                    verticesXCoordinate[j] = verticesZCoordinate[j];
                    verticesZCoordinate[j] = -k;
            }
    }

    public void rotateX(int i) {
            int k = SINE[i];
            int l = COSINE[i];
            for (int i1 = 0; i1 < numberOfVerticeCoordinates; i1++) {
                    int j1 = verticesYCoordinate[i1] * l - verticesZCoordinate[i1] * k >> 16;
                    verticesZCoordinate[i1] = verticesYCoordinate[i1] * k + verticesZCoordinate[i1] * l >> 16;
                    verticesYCoordinate[i1] = j1;
            }
    }

    public void translate(int i, int j, int l) {
            for (int i1 = 0; i1 < numberOfVerticeCoordinates; i1++) {
                    verticesXCoordinate[i1] += i;
                    verticesYCoordinate[i1] += j;
                    verticesZCoordinate[i1] += l;
            }
    }

    public void recolour(int i, int j) {
            if (face_color != null)
                    for (int k = 0; k < numberOfTriangleFaces; k++)
                            if (face_color[k] == (short) i)
                                    face_color[k] = (short) j;
                           
    }
   
    public void setTexture(short i, short j) {
            if (face_texture != null)
                    for (int k = 0; k < numberOfTriangleFaces; k++)
                            if (face_texture[k] == i)
                                    face_texture[k] = j;

    }
   
    public void upscale(int size) {
            for(int i = 0; i < numberOfVerticeCoordinates; i++) {
                    verticesXCoordinate[i] <<= size;
                    verticesYCoordinate[i] <<= size;
                    verticesZCoordinate[i] <<= size;
            }
    }

    public void mirrorModel() {
            for (int j = 0; j < numberOfVerticeCoordinates; j++)
                    verticesZCoordinate[j] = -verticesZCoordinate[j];
            for (int k = 0; k < numberOfTriangleFaces; k++) {
                    int l = face_a[k];
                    face_a[k] = face_c[k];
                    face_c[k] = l;
            }
    }

    public void scaleT(int i, int j, int l) {
            for (int i1 = 0; i1 < numberOfVerticeCoordinates; i1++) {
                    verticesXCoordinate[i1] = (verticesXCoordinate[i1] * i) / 128;
                    verticesYCoordinate[i1] = (verticesYCoordinate[i1] * l) / 128;
                    verticesZCoordinate[i1] = (verticesZCoordinate[i1] * j) / 128;
            }

    }

    public void light(int i, int j, int k, int l, int i1, boolean flag)
    {
            light(i, j, k, l, i1, flag, false, false, true);
    }
   
    public final void light(int i, int j, int k, int l, int i1, boolean flag, boolean fix)
    {
            light(i, j, k, l, i1, flag, fix, false, true);
    }
   
    public final void light(int i, int j, int k, int l, int i1, boolean flag, boolean fix, boolean texture)
    {
            light(i, j, k, l, i1, flag, fix, texture, false);
    }
   
    public final void light(int i, int j, int k, int l, int i1, boolean flag, boolean fixPriorities, boolean texture, boolean color_fix)
    {
            display_model_specific_texture = texture;
            int j1 = (int) Math.sqrt(k * k + l * l + i1 * i1);
            int k1 = j * j1 >> 8;
            if (face_shade_a == null) {
                    face_shade_a = new int[numberOfTriangleFaces];
                    face_shade_b = new int[numberOfTriangleFaces];
                    face_shade_c = new int[numberOfTriangleFaces];
            }
            if (super.vertexNormals == null) {
                    super.vertexNormals = new VertexNormal[numberOfVerticeCoordinates];
                    for (int l1 = 0; l1 < numberOfVerticeCoordinates; l1++)
                            super.vertexNormals[l1] = new VertexNormal();

            }
            for (int i2 = 0; i2 < numberOfTriangleFaces; i2++) {
                    if(color_fix) {
                            if (face_color != null && face_alpha != null) {//Triangle fix
                                    if (face_color[i2] == 65535 || face_color[i2] == 16705 || face_color[i2] == 0)
                                            face_alpha[i2] = 255;
                            }
                    }
                    if (face_render_priorities != null && fixPriorities) {
                            face_render_priorities[i2] = 10;
                    }
                   
                    int j2 = face_a[i2] & 0xffff;
                    int l2 = face_b[i2] & 0xffff;
                    int i3 = face_c[i2] & 0xffff;
                    int j3 = (verticesXCoordinate[l2] - verticesXCoordinate[j2] + 1) >> 2;
                    int k3 = (verticesYCoordinate[l2] - verticesYCoordinate[j2] + 1) >> 2;
                    int l3 = (verticesZCoordinate[l2] - verticesZCoordinate[j2] + 1) >> 2;
                    int i4 = (verticesXCoordinate[i3] - verticesXCoordinate[j2] + 1) >> 2;
                    int j4 = (verticesYCoordinate[i3] - verticesYCoordinate[j2] + 1) >> 2;
                    int k4 = (verticesZCoordinate[i3] - verticesZCoordinate[j2] + 1) >> 2;
                    int l4 = k3 * k4 - j4 * l3;
                    int i5 = l3 * i4 - k4 * j3;
                    int j5;
                    for (j5 = j3 * j4 - i4 * k3; l4 > 8192 || i5 > 8192 || j5 > 8192
                            || l4 < -8192 || i5 < -8192 || j5 < -8192; j5 >>= 1)
                    {
                                    l4 >>= 1;
                                    i5 >>= 1;
                    }

                    int k5 = (int) Math.sqrt(l4 * l4 + i5 * i5 + j5 * j5);
                    if (k5 <= 0)
                            k5 = 1;
                    l4 = (l4 * 256) / k5;
                    i5 = (i5 * 256) / k5;
                    j5 = (j5 * 256) / k5;

                    if (face_render_type == null || (face_render_type[i2] & 1) == 0) {

                            VertexNormal class33_2 = super.vertexNormals[j2];
                            class33_2.anInt602 += l4;
                            class33_2.anInt603 += i5;
                            class33_2.anInt604 += j5;
                            class33_2.anInt605++;
                            class33_2 = super.vertexNormals[l2];
                            class33_2.anInt602 += l4;
                            class33_2.anInt603 += i5;
                            class33_2.anInt604 += j5;
                            class33_2.anInt605++;
                            class33_2 = super.vertexNormals[i3];
                            class33_2.anInt602 += l4;
                            class33_2.anInt603 += i5;
                            class33_2.anInt604 += j5;
                            class33_2.anInt605++;

                    } else {

                            int l5 = i + (k * l4 + l * i5 + i1 * j5) / (k1 + k1 / 2);
                            face_shade_a[i2] = method481(face_color[i2] & 0xffff, l5,
                                            face_render_type[i2]);

                    }
            }
           
            if (flag) {
                    method480(i, k1, k, l, i1);
                    calculateDiagonals();
            } else {
                    vertexNormalOffset = new VertexNormal[numberOfVerticeCoordinates];
                    for (int k2 = 0; k2 < numberOfVerticeCoordinates; k2++) {
                            VertexNormal class33 = super.vertexNormals[k2];
                            VertexNormal class33_1 = vertexNormalOffset[k2] = new VertexNormal();
                            class33_1.anInt602 = class33.anInt602;
                            class33_1.anInt603 = class33.anInt603;
                            class33_1.anInt604 = class33.anInt604;
                            class33_1.anInt605 = class33.anInt605;
                    }
                    calcDiagonalsAndStats(21073);
            }
    }
   
    public final void light_1(int mod, int mag, int x, int y, int z, boolean flat, boolean fix_priorities, boolean texture, boolean color_fix)
    {
            display_model_specific_texture = texture;
            int pre_mag = (int) Math.sqrt((x * x) + (y * y) + (z * z));
            int current_mag = (mag * pre_mag) >> 8;
            if(face_shade_a == null)
            {
                    face_shade_a = new int[numberOfTriangleFaces];
                    face_shade_b = new int[numberOfTriangleFaces];
                    face_shade_c = new int[numberOfTriangleFaces];
                   
            }
            if (super.vertexNormals == null) {
                    vertexNormals = new VertexNormal[numberOfVerticeCoordinates];
                    for (int l1 = 0; l1 < numberOfVerticeCoordinates; l1++)
                            vertexNormals[l1] = new VertexNormal();

            }
            for(int triangle_index = 0; triangle_index < numberOfTriangleFaces; triangle_index++)
            {
                    if(color_fix)
                    {
                            if (face_color != null && face_alpha != null) {//Triangle fix
                                    if (face_color[triangle_index] == 65535 || face_color[triangle_index] == 16705 || face_color[triangle_index] == 0)
                                            face_alpha[triangle_index] = 255;
                            }
                    }
                    if (face_render_priorities != null && fix_priorities)
                    {
                            face_render_priorities[triangle_index] = 10;
                    }
                    int j2 = face_a[triangle_index] & 0xffff;
                    int l2 = face_b[triangle_index] & 0xffff;
                    int i3 = face_c[triangle_index] & 0xffff;
                    int j3 = (verticesXCoordinate[l2] - verticesXCoordinate[j2] + 1) >> 2;
                    int k3 = (verticesYCoordinate[l2] - verticesYCoordinate[j2] + 1) >> 2;
                    int l3 = (verticesZCoordinate[l2] - verticesZCoordinate[j2] + 1) >> 2;
                    int i4 = (verticesXCoordinate[i3] - verticesXCoordinate[j2] + 1) >> 2;
                    int j4 = (verticesYCoordinate[i3] - verticesYCoordinate[j2] + 1) >> 2;
                    int k4 = (verticesZCoordinate[i3] - verticesZCoordinate[j2] + 1) >> 2;
                    int l4 = k3 * k4 - j4 * l3;
                    int i5 = l3 * i4 - k4 * j3;
                    int j5;
                    for (j5 = j3 * j4 - i4 * k3; l4 > 8192 || i5 > 8192 || j5 > 8192
                            || l4 < -8192 || i5 < -8192 || j5 < -8192; j5 >>= 1)
                    {
                                    l4 >>= 1;
                                    i5 >>= 1;
                    }

                    int k5 = (int) Math.sqrt(l4 * l4 + i5 * i5 + j5 * j5);
                    if (k5 <= 0)
                            k5 = 1;
                    l4 = (l4 * 256) / k5;
                    i5 = (i5 * 256) / k5;
                    j5 = (j5 * 256) / k5;

                    if (face_render_type == null || (face_render_type[triangle_index] & 1) == 0)
                    {
                            VertexNormal class33_2 = vertexNormals[j2];
                            class33_2.anInt602 += l4;
                            class33_2.anInt603 += i5;
                            class33_2.anInt604 += j5;
                            class33_2.anInt605++;
                            class33_2 = vertexNormals[l2];
                            class33_2.anInt602 += l4;
                            class33_2.anInt603 += i5;
                            class33_2.anInt604 += j5;
                            class33_2.anInt605++;
                            class33_2 = vertexNormals[i3];
                            class33_2.anInt602 += l4;
                            class33_2.anInt603 += i5;
                            class33_2.anInt604 += j5;
                            class33_2.anInt605++;
                    } else
                    {
                           /* if(triangle_light == null || triangle_light.length != numberOfTriangleFaces)
                            {
                                    triangle_light = new Class34[numberOfTriangleFaces];
                            }
                            Class34 norm = triangle_light[triangle_index] = new Class34();
                            norm.x = l4;
                            norm.y = i5;
                            norm.z = j5;
                           
                            int light = mod +
                                            (((x * triangle_light[triangle_index].x) + (y * triangle_light[triangle_index].y) +
                                            (z * triangle_light[triangle_index].z)) / (mag + (mag / 2)));*/
                            face_shade_a[triangle_index] = method481(face_color[triangle_index] & 0xffff, 0, face_render_type[triangle_index]);
                    }
            }
           
            if(flat)
            {
                    method480(mod, mag, x, y, z, true);
                    calculateDiagonals();
            } else
            {
                    vertexNormalOffset = new VertexNormal[numberOfVerticeCoordinates];
                    for (int coordinate = 0; coordinate < numberOfVerticeCoordinates; coordinate++)
                    {
                            VertexNormal vertex_normal = super.vertexNormals[coordinate];
                            VertexNormal vertex_normal_sub = vertexNormalOffset[coordinate] = new VertexNormal();
                            vertex_normal_sub.anInt602 = vertex_normal.anInt602;
                            vertex_normal_sub.anInt603 = vertex_normal.anInt603;
                            vertex_normal_sub.anInt604 = vertex_normal.anInt604;
                            vertex_normal_sub.anInt605 = vertex_normal.anInt605;
                    }
                    calcDiagonalsAndStats(21073);
            }
    }
   
    public void method480(int intensity, int fall_off, int x, int y, int z)
    {
            method480(intensity, fall_off, x, y, z, false);
    }
   
    public final void method480(int intensity, int fall_off, int x, int y, int z, boolean high_mem)
    {//shade_face(intensity, falloff, x, y, z)
            for (int face = 0; face < numberOfTriangleFaces; face++)
            {
                    int triangle_a = face_a[face];
                    int triangle_b = face_b[face];
                    int triangle_c = face_c[face];
                    if (face_render_type == null) {
                            int triangle_color = face_color[face] & 0xffff;
                            VertexNormal class33 = super.vertexNormals[triangle_a];
                            int light = intensity +
                                    (x * class33.anInt602
                                    + y * class33.anInt603
                                    + z * class33.anInt604) / (fall_off * class33.anInt605);
                            face_shade_a[face] = method481(triangle_color, light, 0);
                           
                            class33 = super.vertexNormals[triangle_b];
                            light = intensity +
                                    (x * class33.anInt602
                                    + y * class33.anInt603
                                    + z * class33.anInt604) / (fall_off * class33.anInt605);
                            face_shade_b[face] = method481(triangle_color, light, 0);
                           
                            class33 = super.vertexNormals[triangle_c];
                            light = intensity
                                    + (x * class33.anInt602
                                    + y * class33.anInt603
                                    + z * class33.anInt604) / (fall_off * class33.anInt605);
                            face_shade_c[face] = method481(triangle_color, light, 0);
                    } else if ((face_render_type[face] & 1) == 0)
                    {
                            int triangle_color = face_color[face] & 0xffff;
                            int flags = face_render_type[face];
                            VertexNormal class33_1 = super.vertexNormals[triangle_a];
                            int light = intensity
                                    + (x * class33_1.anInt602
                                    + y * class33_1.anInt603
                                    + z * class33_1.anInt604) / (fall_off * class33_1.anInt605);
                            face_shade_a[face] = method481(triangle_color, light, flags);
                           
                            class33_1 = super.vertexNormals[triangle_b];
                            light = intensity +
                                    (x * class33_1.anInt602
                                    + y * class33_1.anInt603
                                    + z * class33_1.anInt604) / (fall_off * class33_1.anInt605);
                            face_shade_b[face] = method481(triangle_color, light, flags);
                           
                            class33_1 = super.vertexNormals[triangle_c];
                            light = intensity
                                    + (x * class33_1.anInt602
                                    + y * class33_1.anInt603
                                    + z * class33_1.anInt604) / (fall_off * class33_1.anInt605);
                            face_shade_c[face] = method481(triangle_color, light, flags);
                           
                    }
            }
            if(!high_mem) {
                    super.vertexNormals = null;
                    vertexNormalOffset = null;
                    vertexVSkin = null;
                    triangleTSkin = null;
            }
            if (face_render_type != null) {
                    for (int l1 = 0; l1 < numberOfTriangleFaces; l1++)
                            if ((face_render_type[l1] & 2) == 2)
                                    return;

            }
            //face_color = null;
    }

    public static int method481(int hsl, int light, int flags) {//mixLightness
            //if (hsl == 65535)
            //      return 0;
            if ((flags & 2) == 2) {
                    if (light < 0)
                            light = 0;
                    else if (light > 127)
                            light = 127;
                    light = 127 - light;
                    return light;
            }
            light = light * (hsl & 0x7f) >> 7;
                    if (light < 2)
                            light = 2;
                    else if (light > 126)
                            light = 126;
                    return (hsl & 0xff80) + light;
    }

    public void renderSingle(int j, int k, int l, int i1, int j1, int k1) {
            int i = 0;//constant parameter
            int l1 = Rasterizer.center_x;
            int i2 = Rasterizer.center_y;
            int j2 = SINE[i];
            int k2 = COSINE[i];
            int l2 = SINE[j];
            int i3 = COSINE[j];
            int j3 = SINE[k];
            int k3 = COSINE[k];
            int l3 = SINE[l];
            int i4 = COSINE[l];
            int j4 = j1 * l3 + k1 * i4 >> 16;
            for (int k4 = 0; k4 < numberOfVerticeCoordinates; k4++) {
                    int l4 = verticesXCoordinate[k4];
                    int i5 = verticesYCoordinate[k4];
                    int j5 = verticesZCoordinate[k4];
                    if (k != 0) {
                            int k5 = i5 * j3 + l4 * k3 >> 16;
                            i5 = i5 * k3 - l4 * j3 >> 16;
                            l4 = k5;
                    }
                    if (i != 0) {
                            int l5 = i5 * k2 - j5 * j2 >> 16;
                            j5 = i5 * j2 + j5 * k2 >> 16;
                            i5 = l5;
                    }
                    if (j != 0) {
                            int i6 = j5 * l2 + l4 * i3 >> 16;
                            j5 = j5 * i3 - l4 * l2 >> 16;
                            l4 = i6;
                    }
                    l4 += i1;
                    i5 += j1;
                    j5 += k1;
                    int j6 = i5 * i4 - j5 * l3 >> 16;
                    j5 = i5 * l3 + j5 * i4 >> 16;
                    i5 = j6;
                    projected_vertex_z[k4] = j5 - j4;
                    projected_vertex_x[k4] = l1 + (l4 << 9) / j5;
                    projected_vertex_y[k4] = i2 + (i5 << 9) / j5;
                    if (numberOfTexturedFaces > 0) {
                            camera_vertex_y[k4] = l4;
                            camera_vertex_x[k4] = i5;
                            camera_vertex_z[k4] = j5;
                    }
            }

            try {
                    method483(false, false, 0, -1);
                    return;
            } catch (Exception _ex) {
            	_ex.printStackTrace();
                    return;
            }
    }

    public void renderAtPoint(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int var) {
                    int j2 = l1 * i1 - j1 * l >> 16;
                    int k2 = k1 * j + j2 * k >> 16;
                    int l2 = anInt1650 * k >> 16;
                    int i3 = k2 + l2;
                    if (i3 <= 50 || k2 >= 3500)
                            return;
                    int j3 = l1 * l + j1 * i1 >> 16;
                    int k3 = j3 - anInt1650 << 9;
                    if (k3 / i3 >= DrawingArea.viewport_centerX)
                            return;
                    int l3 = j3 + anInt1650 << 9;
                    if (l3 / i3 <= -DrawingArea.viewport_centerX)
                            return;
                    int i4 = k1 * k - j2 * j >> 16;
                    int j4 = anInt1650 * j >> 16;
                    int k4 = i4 + j4 << 9;
                    if (k4 / i3 <= -DrawingArea.viewport_centerY)
                            return;
                    int l4 = j4 + (super.modelHeight * k >> 16);
                    int i5 = i4 - l4 << 9;
                    if (i5 / i3 >= DrawingArea.viewport_centerY)
                            return;
                    int j5 = l2 + (super.modelHeight * j >> 16);
                    boolean flag = false;
                    if (k2 - j5 <= 50)
                            flag = true;
                    boolean flag1 = false;
                    if (i2 > 0 && objectExists) {
                            int k5 = k2 - l2;
                            if (k5 <= 50)
                                    k5 = 50;
                            if (j3 > 0) {
                                    k3 /= i3;
                                    l3 /= k5;
                            } else {
                                    l3 /= i3;
                                    k3 /= k5;
                            }
                            if (i4 > 0) {
                                    i5 /= i3;
                                    k4 /= k5;
                            } else {
                                    k4 /= i3;
                                    i5 /= k5;
                            }
                            int i6 = currentCursorX - Rasterizer.center_x;
                            int k6 = currentCursorY - Rasterizer.center_y;
                            if (i6 > k3 && i6 < l3 && k6 > i5 && k6 < k4)
                                    if (rendersWithinOneTile) {
                                            mapObjectIds[objectsRendered] = var;
                                            objectsInCurrentRegion[objectsRendered++] = i2;
                                    } else
                                            flag1 = true;
                    }
                    int l5 = Rasterizer.center_x;
                    int j6 = Rasterizer.center_y;
                    int l6 = 0;
                    int i7 = 0;
                    if (i != 0) {
                            l6 = SINE[i];
                            i7 = COSINE[i];
                    }
                    for (int j7 = 0; j7 < numberOfVerticeCoordinates; j7++) {
                            int k7 = verticesXCoordinate[j7];
                            int l7 = verticesYCoordinate[j7];
                            int i8 = verticesZCoordinate[j7];
                            if (i != 0) {
                                    int j8 = i8 * l6 + k7 * i7 >> 16;
                                            i8 = i8 * i7 - k7 * l6 >> 16;
                                            k7 = j8;
                            }
                            k7 += j1;
                            l7 += k1;
                            i8 += l1;
                            int k8 = i8 * l + k7 * i1 >> 16;
                            i8 = i8 * i1 - k7 * l >> 16;
                            k7 = k8;
                            k8 = l7 * k - i8 * j >> 16;
                            i8 = l7 * j + i8 * k >> 16;
                            l7 = k8;
                            projected_vertex_z[j7] = i8 - k2;
                            if (i8 >= 50) {
                                    projected_vertex_x[j7] = l5 + (k7 << 9) / i8;
                                    projected_vertex_y[j7] = j6 + (l7 << 9) / i8;
                            } else {
                                    projected_vertex_x[j7] = -5000;
                                    flag = true;
                            }
                            if (flag || numberOfTexturedFaces > 0) {
                                    camera_vertex_y[j7] = k7;
                                    camera_vertex_x[j7] = l7;
                                    camera_vertex_z[j7] = i8;
                            }
                    }
            try {
                    method483(flag, flag1, i2, var);
                    return;
            } catch (Exception _ex) {
            	_ex.printStackTrace();
                    return;
            }
    }

    private final void method483(boolean flag, boolean flag1, int i, int var) {
            for (int j = 0; j < anInt1652; j++)
                    depthListIndices[j] = 0;

            for (int k = 0; k < numberOfTriangleFaces; k++)
                    if (face_render_type == null || face_render_type[k] != -1) {
                            int l = face_a[k];
                            int k1 = face_b[k];
                            int j2 = face_c[k];
                            int i3 = projected_vertex_x[l];
                            int l3 = projected_vertex_x[k1];
                            int k4 = projected_vertex_x[j2];
                            if (flag && (i3 == -5000 || l3 == -5000 || k4 == -5000)) {
                                    outOfReach[k] = true;
                                    int j5 = (projected_vertex_z[l] + projected_vertex_z[k1] + projected_vertex_z[j2])
                                    / 3 + anInt1653;
                                    faceLists[j5][depthListIndices[j5]++] = k;
                            } else {
                                    if (flag1 && method486(currentCursorX, currentCursorY, projected_vertex_y[l], projected_vertex_y[k1], projected_vertex_y[j2], i3, l3, k4)) {
                                            mapObjectIds[objectsRendered] = var;
                                            objectsInCurrentRegion[objectsRendered++] = i;
                                            flag1 = false;
                                    }
                                    if ((i3 - l3) * (projected_vertex_y[j2] - projected_vertex_y[k1])
                                                    - (projected_vertex_y[l] - projected_vertex_y[k1])
                                                    * (k4 - l3) > 0) {
                                            outOfReach[k] = false;
                                            if (i3 < 0 || l3 < 0 || k4 < 0
                                                            || i3 > DrawingArea.viewportRX
                                                            || l3 > DrawingArea.viewportRX
                                                            || k4 > DrawingArea.viewportRX)
                                                    hasAnEdgeToRestrict[k] = true;
                                            else
                                                    hasAnEdgeToRestrict[k] = false;
                                            int k5 = (projected_vertex_z[l] + projected_vertex_z[k1] + projected_vertex_z[j2])
                                            / 3 + anInt1653;
                                            faceLists[k5][depthListIndices[k5]++] = k;
                                    }
                            }
                    }

            if (face_render_priorities == null) {
                    for (int i1 = anInt1652 - 1; i1 >= 0; i1--) {
                            int l1 = depthListIndices[i1];
                            if (l1 > 0) {
                                    int ai[] = faceLists[i1];
                                    for (int j3 = 0; j3 < l1; j3++)
                                            rasterise(ai[j3]);

                            }
                    }

                    return;
            }
            for (int j1 = 0; j1 < 12; j1++) {
                    anIntArray1673[j1] = 0;
                    anIntArray1677[j1] = 0;
            }

            for (int i2 = anInt1652 - 1; i2 >= 0; i2--) {
                    int k2 = depthListIndices[i2];
                    if (k2 > 0) {
                            int ai1[] = faceLists[i2];
                            for (int i4 = 0; i4 < k2; i4++) {
                                    int l4 = ai1[i4];
                                    int l5 = face_render_priorities[l4];
                                    int j6 = anIntArray1673[l5]++;
                                    anIntArrayArray1674[l5][j6] = l4;
                                    if (l5 < 10)
                                            anIntArray1677[l5] += i2;
                                    else if (l5 == 10)
                                            anIntArray1675[j6] = i2;
                                    else
                                            anIntArray1676[j6] = i2;
                            }

                    }
            }

            int l2 = 0;
            if (anIntArray1673[1] > 0 || anIntArray1673[2] > 0)
                    l2 = (anIntArray1677[1] + anIntArray1677[2])
                    / (anIntArray1673[1] + anIntArray1673[2]);
            int k3 = 0;
            if (anIntArray1673[3] > 0 || anIntArray1673[4] > 0)
                    k3 = (anIntArray1677[3] + anIntArray1677[4])
                    / (anIntArray1673[3] + anIntArray1673[4]);
            int j4 = 0;
            if (anIntArray1673[6] > 0 || anIntArray1673[8] > 0)
                    j4 = (anIntArray1677[6] + anIntArray1677[8])
                    / (anIntArray1673[6] + anIntArray1673[8]);
            int i6 = 0;
            int k6 = anIntArray1673[10];
            int ai2[] = anIntArrayArray1674[10];
            int ai3[] = anIntArray1675;
            if (i6 == k6) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
            }
            int i5;
            if (i6 < k6)
                    i5 = ai3[i6];
            else
                    i5 = -1000;
            for (int l6 = 0; l6 < 10; l6++) {
                    while (l6 == 0 && i5 > l2) {
                            rasterise(ai2[i6++]);
                            if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                                    i6 = 0;
                                    k6 = anIntArray1673[11];
                                    ai2 = anIntArrayArray1674[11];
                                    ai3 = anIntArray1676;
                            }
                            if (i6 < k6)
                                    i5 = ai3[i6];
                            else
                                    i5 = -1000;
                    }
                    while (l6 == 3 && i5 > k3) {
                            rasterise(ai2[i6++]);
                            if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                                    i6 = 0;
                                    k6 = anIntArray1673[11];
                                    ai2 = anIntArrayArray1674[11];
                                    ai3 = anIntArray1676;
                            }
                            if (i6 < k6)
                                    i5 = ai3[i6];
                            else
                                    i5 = -1000;
                    }
                    while (l6 == 5 && i5 > j4) {
                            rasterise(ai2[i6++]);
                            if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                                    i6 = 0;
                                    k6 = anIntArray1673[11];
                                    ai2 = anIntArrayArray1674[11];
                                    ai3 = anIntArray1676;
                            }
                            if (i6 < k6)
                                    i5 = ai3[i6];
                            else
                                    i5 = -1000;
                    }
                    int i7 = anIntArray1673[l6];
                    int ai4[] = anIntArrayArray1674[l6];
                    for (int j7 = 0; j7 < i7; j7++)
                            rasterise(ai4[j7]);

            }

            while (i5 != -1000) {
                    rasterise(ai2[i6++]);
                    if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                            i6 = 0;
                            ai2 = anIntArrayArray1674[11];
                            k6 = anIntArray1673[11];
                            ai3 = anIntArray1676;
                    }
                    if (i6 < k6)
                            i5 = ai3[i6];
                    else
                            i5 = -1000;
            }
    }
   
    private final void rasterise(int face)
    {
            if (outOfReach[face])
            {
                    reduce(face);
                    return;
            }
            int a = face_a[face] & 0xffff;
            int b = face_b[face] & 0xffff;
            int c = face_c[face] & 0xffff;
            if (face_color != null && face_color[face] == -1)
                    return;
            Rasterizer.restrict_edges = hasAnEdgeToRestrict[face];
            Rasterizer.alpha = (face_alpha == null ? 0 : face_alpha[face]);
            int face_type;
            if(face_render_type == null)
            {
                    face_type = 0;
            } else //if(face_render_type[face] != -1)
            {
                    face_type = face_render_type[face] & 0xff;
            }
            boolean noTextureValues = (face_texture == null || face >= face_texture.length ||  face_texture[face] == -1 ? true : false);
            boolean noCoordinates = (texture_coordinates == null || texture_coordinates[face] == -1 ? true : false);
            if(noTextureValues || noCoordinates)
            {
                    if(face_type == 0)
                    {//LD
                            Rasterizer.drawShadedTriangle(
                                    projected_vertex_y[a], projected_vertex_y[b], projected_vertex_y[c],
                                    projected_vertex_x[a], projected_vertex_x[b], projected_vertex_x[c],
                                    face_shade_a[face], face_shade_b[face], face_shade_c[face]
                            );
                            return;
                    }
                    if(face_type == 1)
                    {
                            Rasterizer.drawFlatTriangle(
                                    projected_vertex_y[a], projected_vertex_y[b], projected_vertex_y[c],
                                    projected_vertex_x[a], projected_vertex_x[b], projected_vertex_x[c],
                                    hsl2rgb[face_shade_a[face]]
                            );
                            return;
                    }
            }
           
            if(display_model_specific_texture && Client.getOption("hd_tex"))
            {
                    if(face_type == 0)
                    {
                                    int texture_type = !noCoordinates ? texture_coordinates[face] & 0xff : -1;
                                    if(texture_type == 0xff)
                                            texture_type = -1;
                                    if(texture_type == -1 || texture_render_type == null || texture_render_type[texture_type] >= 0) {
                                            int x = (texture_type == -1 || texture_render_type[texture_type] > 0) ? a : textured_face_a[texture_type];
                                            int y = (texture_type == -1 || texture_render_type[texture_type] > 0) ? b : textured_face_b[texture_type];
                                            int z = (texture_type == -1 || texture_render_type[texture_type] > 0) ? c : textured_face_c[texture_type];
                                            Rasterizer.renderHDModelTexturedTriangle(
                                                    projected_vertex_y[a], projected_vertex_y[b], projected_vertex_y[c],
                                                    projected_vertex_x[a], projected_vertex_x[b], projected_vertex_x[c],
                                                    face_shade_a[face], face_shade_b[face], face_shade_c[face],
                                                    camera_vertex_y[x], camera_vertex_y[y], camera_vertex_y[z],
                                                    camera_vertex_x[x], camera_vertex_x[y], camera_vertex_x[z],
                                                    camera_vertex_z[x], camera_vertex_z[y], camera_vertex_z[z],
                                                    face_texture[face], (face_color != null ? face_color[face] & 0xffff : face_shade_a[face]), force_texture
                                            );
                                            return;
                                    }
                            }
                           
                            if(face_type == 1)
                            {
                                    int texture_type = !noCoordinates ? texture_coordinates[face] & 0xff : -1;
                                    if(texture_type == 0xff)
                                            texture_type = -1;
                                    if(texture_type == -1 || texture_render_type == null || texture_render_type[texture_type] >= 0) {
                                            int x = (texture_type == -1 || texture_render_type[texture_type] > 0) ? a : textured_face_a[texture_type];
                                            int y = (texture_type == -1 || texture_render_type[texture_type] > 0) ? b : textured_face_b[texture_type];
                                            int z = (texture_type == -1 || texture_render_type[texture_type] > 0) ? c : textured_face_c[texture_type];
                                            Rasterizer.renderHDModelTexturedTriangle(
                                                    projected_vertex_y[a], projected_vertex_y[b], projected_vertex_y[c],
                                                    projected_vertex_x[a], projected_vertex_x[b], projected_vertex_x[c],
                                                    face_shade_a[face], face_shade_a[face], face_shade_a[face],
                                                    camera_vertex_y[x], camera_vertex_y[y], camera_vertex_y[z],
                                                    camera_vertex_x[x], camera_vertex_x[y], camera_vertex_x[z],
                                                    camera_vertex_z[x], camera_vertex_z[y], camera_vertex_z[z],
                                                    face_texture[face], (face_color != null ? face_color[face] & 0xffff : face_shade_a[face]), force_texture
                                            );
                                            return;
                                    }
                            }
           
            }
   
            if(face_type == 0)
            {//LD
                    Rasterizer.drawShadedTriangle(
                            projected_vertex_y[a], projected_vertex_y[b], projected_vertex_y[c],
                            projected_vertex_x[a], projected_vertex_x[b], projected_vertex_x[c],
                            face_shade_a[face], face_shade_b[face], face_shade_c[face]
                    );
                    return;
            }
            if(face_type == 1)
            {
                    Rasterizer.drawFlatTriangle(
                            projected_vertex_y[a], projected_vertex_y[b], projected_vertex_y[c],
                            projected_vertex_x[a], projected_vertex_x[b], projected_vertex_x[c],
                            hsl2rgb[face_shade_a[face]]
                    );
                    return;
            }
    }
   
    private final void reduce(int i)
    {
            if (face_color != null && face_color[i] == -1)
                            return;
            int j = Rasterizer.center_x;
            int k = Rasterizer.center_y;
            int l = 0;
            int i1 = face_a[i];
            int j1 = face_b[i];
            int k1 = face_c[i];
            int l1 = camera_vertex_z[i1];
            int i2 = camera_vertex_z[j1];
            int j2 = camera_vertex_z[k1];
            

            if (l1 >= 50) {
                    anIntArray1678[l] = projected_vertex_x[i1];
                    anIntArray1679[l] = projected_vertex_y[i1];
                    anIntArray1680[l++] = face_shade_a[i];
            } else {
                    int k2 = camera_vertex_y[i1];
                    int k3 = camera_vertex_x[i1];
                    int k4 = face_shade_a[i];
                    if (j2 >= 50) {
                            int k5 = (50 - l1) * lightDecay[j2 - l1];
                            anIntArray1678[l] = j
                            + (k2 + ((camera_vertex_y[k1] - k2) * k5 >> 16) << 9)
                            / 50;
                            anIntArray1679[l] = k
                            + (k3 + ((camera_vertex_x[k1] - k3) * k5 >> 16) << 9)
                            / 50;
                            anIntArray1680[l++] = k4
                            + ((face_shade_c[i] - k4) * k5 >> 16);
                    }
                    if (i2 >= 50) {
                            int l5 = (50 - l1) * lightDecay[i2 - l1];
                            anIntArray1678[l] = j
                            + (k2 + ((camera_vertex_y[j1] - k2) * l5 >> 16) << 9)
                            / 50;
                            anIntArray1679[l] = k
                            + (k3 + ((camera_vertex_x[j1] - k3) * l5 >> 16) << 9)
                            / 50;
                            anIntArray1680[l++] = k4
                            + ((face_shade_b[i] - k4) * l5 >> 16);
                    }
            }
            if (i2 >= 50) {
                    anIntArray1678[l] = projected_vertex_x[j1];
                    anIntArray1679[l] = projected_vertex_y[j1];
                    anIntArray1680[l++] = face_shade_b[i];
            } else {
                    int l2 = camera_vertex_y[j1];
                    int l3 = camera_vertex_x[j1];
                    int l4 = face_shade_b[i];
                    if (l1 >= 50) {
                            int i6 = (50 - i2) * lightDecay[l1 - i2];
                            anIntArray1678[l] = j
                            + (l2 + ((camera_vertex_y[i1] - l2) * i6 >> 16) << 9)
                            / 50;
                            anIntArray1679[l] = k
                            + (l3 + ((camera_vertex_x[i1] - l3) * i6 >> 16) << 9)
                            / 50;
                            anIntArray1680[l++] = l4
                            + ((face_shade_a[i] - l4) * i6 >> 16);
                    }
                    if (j2 >= 50) {
                            int j6 = (50 - i2) * lightDecay[j2 - i2];
                            anIntArray1678[l] = j
                            + (l2 + ((camera_vertex_y[k1] - l2) * j6 >> 16) << 9)
                            / 50;
                            anIntArray1679[l] = k
                            + (l3 + ((camera_vertex_x[k1] - l3) * j6 >> 16) << 9)
                            / 50;
                            anIntArray1680[l++] = l4
                            + ((face_shade_c[i] - l4) * j6 >> 16);
                    }
            }
            if (j2 >= 50) {
                    anIntArray1678[l] = projected_vertex_x[k1];
                    anIntArray1679[l] = projected_vertex_y[k1];
                    anIntArray1680[l++] = face_shade_c[i];
            } else {
                    int i3 = camera_vertex_y[k1];
                    int i4 = camera_vertex_x[k1];
                    int i5 = face_shade_c[i];
                    if (i2 >= 50) {
                            int k6 = (50 - j2) * lightDecay[i2 - j2];
                            anIntArray1678[l] = j
                            + (i3 + ((camera_vertex_y[j1] - i3) * k6 >> 16) << 9)
                            / 50;
                            anIntArray1679[l] = k
                            + (i4 + ((camera_vertex_x[j1] - i4) * k6 >> 16) << 9)
                            / 50;
                            anIntArray1680[l++] = i5
                            + ((face_shade_b[i] - i5) * k6 >> 16);
                    }
                    if (l1 >= 50) {
                            int l6 = (50 - j2) * lightDecay[l1 - j2];
                            anIntArray1678[l] = j
                            + (i3 + ((camera_vertex_y[i1] - i3) * l6 >> 16) << 9)
                            / 50;
                            anIntArray1679[l] = k
                            + (i4 + ((camera_vertex_x[i1] - i4) * l6 >> 16) << 9)
                            / 50;
                            anIntArray1680[l++] = i5
                            + ((face_shade_a[i] - i5) * l6 >> 16);
                    }
            }
            int j3 = anIntArray1678[0];
            int j4 = anIntArray1678[1];
            int j5 = anIntArray1678[2];
            int i7 = anIntArray1679[0];
            int j7 = anIntArray1679[1];
            int k7 = anIntArray1679[2];
            if ((j3 - j4) * (k7 - j7) - (i7 - j7) * (j5 - j4) > 0) {
                    Rasterizer.restrict_edges = false;
                    if (l == 3) {
                            if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > DrawingArea.viewportRX
                                            || j4 > DrawingArea.viewportRX || j5 > DrawingArea.viewportRX)
                                    Rasterizer.restrict_edges = true;
                           
                           
                            int meshType;
                            if (face_render_type == null)
                                    meshType = 0;
                            else
                                    meshType = face_render_type[i] & 3;
                                   
                            Rasterizer.alpha = (face_alpha == null ? 0 : face_alpha[i]);
                            if (meshType == 0)//LD
                                    Rasterizer.drawShadedTriangle(i7, j7, k7, j3, j4, j5,
                                                    anIntArray1680[0], anIntArray1680[1],
                                                    anIntArray1680[2]);
                            else if (meshType == 1)
                                    Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5, hsl2rgb[face_shade_a[i]]);
                            else if (meshType == 2) {
                                    int j8 = face_render_type[i] >> 2;
                                    int k9 = textured_face_a[j8];
                                    int k10 = textured_face_b[j8];
                                    int k11 = textured_face_c[j8];
                                    try {
                                    Rasterizer.renderHDModelTexturedTriangle(i7, j7, k7, j3, j4, j5,
                                                    anIntArray1680[0], anIntArray1680[1],
                                                    anIntArray1680[2], camera_vertex_y[k9],
                                                    camera_vertex_y[k10], camera_vertex_y[k11],
                                                    camera_vertex_x[k9], camera_vertex_x[k10],
                                                    camera_vertex_x[k11], camera_vertex_z[k9],
                                                    camera_vertex_z[k10], camera_vertex_z[k11],
                                                    face_texture[i], (face_color != null ? face_color[i] & 0xffff : face_shade_a[i]), false);
                                    } catch(Exception e) {}
                            } else if (meshType == 3) {
                                    int k8 = face_render_type[i] >> 2;
                                    int l9 = textured_face_a[k8];
                                    int l10 = textured_face_b[k8];
                                    int l11 = textured_face_c[k8];
                                    Rasterizer.renderHDModelTexturedTriangle(i7, j7, k7, j3, j4, j5,
                                                    face_shade_a[i], face_shade_a[i],
                                                    face_shade_a[i], camera_vertex_y[l9],
                                                    camera_vertex_y[l10], camera_vertex_y[l11],
                                                    camera_vertex_x[l9], camera_vertex_x[l10],
                                                    camera_vertex_x[l11], camera_vertex_z[l9],
                                                    camera_vertex_z[l10], camera_vertex_z[l11],
                                                    face_texture[i], (face_color != null ? face_color[i] & 0xffff : face_shade_a[i]), false);
                            }
                    }
                    if (l == 4) {
                            if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > DrawingArea.viewportRX
                                            || j4 > DrawingArea.viewportRX || j5 > DrawingArea.viewportRX
                                            || anIntArray1678[3] < 0
                                            || anIntArray1678[3] > DrawingArea.viewportRX)
                                    Rasterizer.restrict_edges = true;
                            int i8;
                            if (face_render_type == null)
                                    i8 = 0;
                            else
                                    i8 = face_render_type[i] & 3;
                            Rasterizer.alpha = (face_alpha == null ? 0 : face_alpha[i]);
                            if (i8 == 0) {
                                    Rasterizer.drawShadedTriangle(i7, j7, k7, j3, j4, j5,
                                                    anIntArray1680[0], anIntArray1680[1],
                                                    anIntArray1680[2]);//LD
                                    Rasterizer.drawShadedTriangle(i7, k7, anIntArray1679[3], j3, j5,
                                                    anIntArray1678[3], anIntArray1680[0],
                                                    anIntArray1680[2], anIntArray1680[3]);//LD
                                    return;
                            }
                            if (i8 == 1) {
                                    int l8 = hsl2rgb[face_shade_a[i]];
                                    Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5, l8);
                                    Rasterizer.drawFlatTriangle(i7, k7, anIntArray1679[3], j3, j5,
                                                    anIntArray1678[3], l8);
                                    return;
                            }
                            if (i8 == 2) {
                                    int i9 = face_render_type[i] >> 2;
                                    int i10 = textured_face_a[i9];
                                    int i11 = textured_face_b[i9];
                                    int i12 = textured_face_c[i9];
                                    try {
                                    Rasterizer.renderHDModelTexturedTriangle(i7, j7, k7, j3, j4, j5,
                                                    anIntArray1680[0], anIntArray1680[1],
                                                    anIntArray1680[2], camera_vertex_y[i10],
                                                    camera_vertex_y[i11], camera_vertex_y[i12],
                                                    camera_vertex_x[i10], camera_vertex_x[i11],
                                                    camera_vertex_x[i12], camera_vertex_z[i10],
                                                    camera_vertex_z[i11], camera_vertex_z[i12],
                                                    face_texture[i], (face_color != null ? face_color[i] & 0xffff : face_shade_a[i]), false);
                                    
                                    Rasterizer.renderHDModelTexturedTriangle(i7, k7, anIntArray1679[3], j3, j5,
                                                    anIntArray1678[3], anIntArray1680[0],
                                                    anIntArray1680[2], anIntArray1680[3],
                                                    camera_vertex_y[i10], camera_vertex_y[i11],
                                                    camera_vertex_y[i12], camera_vertex_x[i10],
                                                    camera_vertex_x[i11], camera_vertex_x[i12],
                                                    camera_vertex_z[i10], camera_vertex_z[i11],
                                                    camera_vertex_z[i12], face_texture[i], (face_color != null ? face_color[i] & 0xffff : face_shade_a[i]), false);
                                    } catch(Exception e) {}
                                    return;
                            }
                            if (i8 == 3) {
                                    int j9 = face_render_type[i] >> 2;
                                    int j10 = textured_face_a[j9];
                                    int j11 = textured_face_b[j9];
                                    int j12 = textured_face_c[j9];
                                    Rasterizer.renderHDModelTexturedTriangle(i7, j7, k7, j3, j4, j5,
                                                    face_shade_a[i], face_shade_a[i],
                                                    face_shade_a[i], camera_vertex_y[j10],
                                                    camera_vertex_y[j11], camera_vertex_y[j12],
                                                    camera_vertex_x[j10], camera_vertex_x[j11],
                                                    camera_vertex_x[j12], camera_vertex_z[j10],
                                                    camera_vertex_z[j11], camera_vertex_z[j12],
                                                    face_texture[i], (face_color != null ? face_color[i] & 0xffff : face_shade_a[i]), false);
                                    Rasterizer.renderHDModelTexturedTriangle(i7, k7, anIntArray1679[3], j3, j5,
                                                    anIntArray1678[3], face_shade_a[i],
                                                    face_shade_a[i], face_shade_a[i],
                                                    camera_vertex_y[j10], camera_vertex_y[j11],
                                                    camera_vertex_y[j12], camera_vertex_x[j10],
                                                    camera_vertex_x[j11], camera_vertex_x[j12],
                                                    camera_vertex_z[j10], camera_vertex_z[j11],
                                                    camera_vertex_z[j12], face_texture[i], (face_color != null ? face_color[i] & 0xffff : face_shade_a[i]), false);
                            }
                    }
            }
    }

    private final boolean method486(int i, int j, int k, int l, int i1, int j1,
                    int k1, int l1) {
            if (j < k && j < l && j < i1)
                    return false;
            if (j > k && j > l && j > i1)
                    return false;
            if (i < j1 && i < k1 && i < l1)
                    return false;
            return i <= j1 || i <= k1 || i <= l1;
    }
   
    public static int anInt1620;
   
    public Rasterizer rasterizer;
    public boolean is_entity;
    public byte texture_coordinates[];                                                              //!texturePointers
    public byte texture_render_type[];                                                              //!textureDrawTypes
    public short face_texture[];                                                                    //!textureBackground // textureContainer
    public boolean force_texture;
    public boolean display_model_specific_texture;
   
    public static HDModel entityModelDesc = new HDModel();
    private static short anIntArray1626[] = new short[2000];
    public int numberOfVerticeCoordinates;//vertexCount
    public int verticesXCoordinate[];//verticesXCoordinate
    public int verticesYCoordinate[];//verticesYCoordinate
    public int verticesZCoordinate[];//verticesZCoordinate
    public int numberOfTriangleFaces;//triangleCount
    public int face_a[];//triangleA
    public int face_b[];//triangleB
    public int face_c[];//triangleC
    public int face_shade_a[];//triangleHslA
    public int face_shade_b[];//triangleHslB
    public int face_shade_c[];//triangleHslC
    public byte face_render_type[];//triangleDrawType
    public int face_render_priorities[];//facePriority
    public int face_alpha[];//TriangleAlpha
    public short face_color[];//triangleColorOrTexture
    public int face_priority;
    public int numberOfTexturedFaces;//textureTriangleCount
    public int textured_face_a[];//trianglePIndex
    public int textured_face_b[];//triangleMIndex
    public int textured_face_c[];//triangleNIndex
    public int anInt1646;//minX
    public int anInt1647;//maxX
    public int anInt1648;//maxZ
    public int anInt1649;//minZ
    public int anInt1650;//diagnol2DAboveorigin
    public int anInt1651;//maxY
    public int anInt1652;//diagonal3D
    public int anInt1653;//diagonal3DAboveorigin
    public int myPriority;
    public int vertexVSkin[];//vertexVSkin
    public int triangleTSkin[];//triangleTSkin
    public int vertexSkin[][];//vertexSkin
    public int triangleSkin[][];//triangleSkin
    public boolean rendersWithinOneTile;//oneSquareModel
    VertexNormal vertexNormalOffset[];//vertexNormalOffset

    static {
    }
}
