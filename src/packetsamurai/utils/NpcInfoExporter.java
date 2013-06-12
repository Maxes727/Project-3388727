package packetsamurai.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javolution.util.FastList;

import packetsamurai.PacketSamurai;
import packetsamurai.parser.datatree.ValuePart;
import packetsamurai.session.DataPacket;


/**
 * @author ATracer - Kamui
 */
public class NpcInfoExporter
{
	private List<DataPacket> packets;
	private String sessionName;
	private FastList<NpcInfo> npcInfoList = new FastList<NpcInfo>();

	public NpcInfoExporter(List<DataPacket> packets, String sessionName) 
	{
		super();
		this.packets = packets;
		this.sessionName = sessionName;
	}

	public void parse()
	{
		String fileName = "npcinfo.txt";

		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));

			for(DataPacket packet : packets)
			{
				String name = packet.getName();
				if("SM_NPC_INFO".equals(name))
				{
					List<ValuePart> valuePartList = packet.getValuePartList();
					NpcInfo npc = new NpcInfo();

					for(ValuePart valuePart : valuePartList)
					{
						String partName = valuePart.getModelPart().getName();
						if("npcId".equals(partName))
						{
							npc.npcId = Integer.parseInt(valuePart.readValue());
						}
						else if("static_id".equals(partName))
						{
							npc.static_id = Integer.parseInt(valuePart.readValue());
						}
						else if("npcTemplateNameId".equals(partName))
						{
							npc.npcTemplateNameId = Integer.parseInt(valuePart.readValue());
						}
						else if("npcTemplateTitleId".equals(partName))
						{
							npc.npcTemplateTitleId = Integer.parseInt(valuePart.readValue());
						}
                                                                                                else if("realhp".equals(partName)){
                                                                                                                npc.realhp = Integer.parseInt(valuePart.readValue());
                                                                                                }
						else if("npcState".equals(partName))
						{
							npc.npcMode = Integer.parseInt(valuePart.readValue());
						}
						else if("moveType".equals(partName))
						{
							npc.moveType = Integer.parseInt(valuePart.readValue());
						}
						else if("x".equals(partName)) 
						{
							npc.x = Float.parseFloat(valuePart.readValue());
						}
						else if("y".equals(partName)) 
						{
							npc.y = Float.parseFloat(valuePart.readValue());
						}
						else if("z".equals(partName)) 
						{
							npc.z = Float.parseFloat(valuePart.readValue());
						}
						else if("npcHeading".equals(partName))
						{
							npc.npcHeading = Integer.parseInt(valuePart.readValue());
						}
						else if("level".equals(partName)) 
						{
							npc.level = Byte.parseByte(valuePart.readValue());
						}
						else if("npcType".equals(partName)) 
						{
							switch(Integer.parseInt(valuePart.readValue()))
							{
								case 0:
									npc.npcType = "ATTACKABLE";
								break;
								case 2:
									npc.npcType = "NEUTRAL";
								break;
								case 8:
									npc.npcType = "AGGRESSIVE";
								break;
								case 38:
									npc.npcType = "NON_ATTACKABLE";
								break;
								default:
									npc.npcType = valuePart.readValue();
								break;
							}
						}
					}
					npcInfoList.add(npc);
				}
			}

			for(NpcInfo npc : npcInfoList)
			{
				StringBuilder sb = new StringBuilder();
                                 
				String line = "" + npc.npcId + "," + npc.level + "," + npc.moveType + "," + npc.npcHeading + "," + npc.npcId + "," + npc.npcMode + "," + npc.npcTemplateNameId + "," + npc.npcTemplateTitleId + "," + npc.realhp + "," + npc.static_id + "," + npc.x + "," + npc.y + "," + npc.z;
                                                                sb.append("\n" + line);
				out.write(sb.toString());
			}
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		PacketSamurai.getUserInterface().log("NPC Infos Has Been Written Successful");
	}

	class NpcInfo
	{
		int npcId;
		int static_id;
		int npcTemplateNameId;
		int npcTemplateTitleId;
		int npcMode;
		int moveType;
		String npcType;
		float x;
		float y;
		float z;
		int npcHeading;
		byte level;
                                int realhp;
	}
}
