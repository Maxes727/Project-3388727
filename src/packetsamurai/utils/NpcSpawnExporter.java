package packetsamurai.utils;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javolution.util.FastList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import packetsamurai.PacketSamurai;
import packetsamurai.parser.datatree.ValuePart;
import packetsamurai.session.DataPacket;

import com.sun.org.apache.xpath.internal.XPathAPI;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.w3c.dom.Comment;

/**
 * Exports NPCs from sniffer knowlist to emulator compatible XML file
 * 
 * @author lhw, Kamui
 */
public class NpcSpawnExporter
{
	private FastList<DataPacket> packets;
	private FastList<NpcSpawn> spawns;
	private String sessionName;
	private int worldId = -1;

	public NpcSpawnExporter(List<DataPacket> packets, String sessionName)
	{
		this.packets = new FastList<DataPacket>(packets);
		this.sessionName = sessionName;
		this.spawns = new FastList<NpcSpawn>();
	}

	public void parse()
	{
		String filename = "npcpointdata_seq_" + sessionName + ".txt";
		Long start = System.currentTimeMillis();

		try
		{

			// Collect info about all seen NPCs
			for (DataPacket packet : packets)
			{
				String packetName = packet.getName();
				if ("SM_PLAYER_SPAWN".equals(packetName))
					this.worldId = Integer.parseInt(packet.getValuePartList().get(1).readValue());
				else if ("SM_NPC_INFO".equals(packetName))
				{
					NpcSpawn spawn = new NpcSpawn();
					FastList<ValuePart> valuePartList = new FastList<ValuePart>(packet.getValuePartList());
					for (ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if ("x".equals(partName))
							spawn.x = Float.parseFloat(valuePart.readValue());
						else if ("y".equals(partName))
							spawn.y = Float.parseFloat(valuePart.readValue());
						else if ("z".equals(partName))
							spawn.z = Float.parseFloat(valuePart.readValue());
						else if ("npcId".equals(partName))
							spawn.npcId = Integer.parseInt(valuePart.readValue());
						else if ("npcHeading".equals(partName))
							spawn.heading = Byte.parseByte(valuePart.readValue());
						else if ("staticid".equals(partName))
							spawn.staticid = Integer.parseInt(valuePart.readValue());
						else if ("objId".equals(partName))
							spawn.objectId = Long.parseLong(valuePart.readValue());
						spawn.worldId = this.worldId;
					}
					boolean exists = false;
					for (NpcSpawn n : spawns)
						if (n.objectId == spawn.objectId)
							exists = true;
					if (!exists)
						spawns.add(spawn);
				}
			}
                                                StringBuilder builder = new StringBuilder();
			for (NpcSpawn n : spawns)
			{
			                 String line = n.npcId + "," + n.worldId + "," + n.objectId + "," + n.x + "," + n.y + "," + n.z + "," + n.staticid + "," + n.heading;
                                                                 builder.append("\n"+line);
 			}
                                                FileWriter writer = new FileWriter(filename);
                                                PrintWriter out = new PrintWriter(writer);
                                                out.print(builder.toString());
                                                out.close();
		}
                                catch(Exception ex){
                                }
		PacketSamurai.getUserInterface().log("NPC spawndata has been written in " + ((float) (System.currentTimeMillis() - start) / 1000) + "s");
	}

	class NpcSpawn
	{
		float x;
		float y;
		float z;
		int npcId;
		long objectId;
		byte heading;
		int worldId;
		int staticid;
	}
}
