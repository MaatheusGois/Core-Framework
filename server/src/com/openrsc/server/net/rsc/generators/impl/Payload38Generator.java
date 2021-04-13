package com.openrsc.server.net.rsc.generators.impl;

import com.openrsc.server.external.GameObjectLoc;
import com.openrsc.server.external.ItemLoc;
import com.openrsc.server.model.Point;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.net.Packet;
import com.openrsc.server.net.PacketBuilder;
import com.openrsc.server.net.rsc.PayloadValidator;
import com.openrsc.server.net.rsc.enums.OpcodeOut;
import com.openrsc.server.net.rsc.generators.PayloadGenerator;
import com.openrsc.server.net.rsc.struct.AbstractStruct;
import com.openrsc.server.net.rsc.struct.outgoing.*;
import com.openrsc.server.util.rsc.DataConversions;

import java.util.HashMap;
import java.util.Map;

/**
 * RSC Protocol-38 Generator for Outgoing Packets from respective Protocol Independent Structs
 * **/
public class Payload38Generator implements PayloadGenerator<OpcodeOut> {
	private static final Map<OpcodeOut, Integer> opcodeMap = new HashMap<OpcodeOut, Integer>() {{
		put(OpcodeOut.SEND_SERVER_MESSAGE, 8);
		put(OpcodeOut.SEND_FRIEND_LIST, 23);
		put(OpcodeOut.SEND_FRIEND_UPDATE, 24);
		put(OpcodeOut.SEND_IGNORE_LIST, 26);
		put(OpcodeOut.SEND_PRIVACY_SETTINGS, 27);
		put(OpcodeOut.SEND_PRIVATE_MESSAGE, 28);
		put(OpcodeOut.SEND_GAME_SETTINGS, 228);
		put(OpcodeOut.SEND_TRADE_ACCEPTED, 229);
		put(OpcodeOut.RUNESCAPE_UPDATED, 230); // won't be necessary to be used
		put(OpcodeOut.SEND_REMOVE_WORLD_NPC, 231);
		put(OpcodeOut.SEND_REMOVE_WORLD_PLAYER, 232);
		put(OpcodeOut.SEND_SHOP_CLOSE, 233);
		put(OpcodeOut.SEND_SHOP_OPEN, 234);
		put(OpcodeOut.SEND_TRADE_OTHER_ACCEPTED, 235);
		put(OpcodeOut.SEND_TRADE_OTHER_ITEMS, 236);
		put(OpcodeOut.SEND_TRADE_CLOSE, 237);
		put(OpcodeOut.SEND_TRADE_WINDOW, 238);
		put(OpcodeOut.SEND_APPEARANCE_CHANGE, 239);
		put(OpcodeOut.SEND_REMOVE_WORLD_ENTITY, 240);
		put(OpcodeOut.SEND_DEATH, 241);
		put(OpcodeOut.SEND_EQUIPMENT_STATS, 242);
		put(OpcodeOut.SEND_STATS, 243);
		put(OpcodeOut.SEND_WORLD_INFO, 244);
		put(OpcodeOut.SEND_OPTIONS_MENU_CLOSE, 245);
		put(OpcodeOut.SEND_OPTIONS_MENU_OPEN, 246);
		put(OpcodeOut.SEND_UPDATE_NPC, 247);
		put(OpcodeOut.SEND_NPC_COORDS, 248);
		put(OpcodeOut.SEND_BOUNDARY_HANDLER, 249);
		put(OpcodeOut.SEND_UPDATE_PLAYERS, 250);
		put(OpcodeOut.SEND_UPDATE_PLAYERS_RETRO, 251);
		put(OpcodeOut.SEND_INVENTORY, 252);
		put(OpcodeOut.SEND_SCENERY_HANDLER, 253);
		put(OpcodeOut.SEND_GROUND_ITEM_HANDLER, 254);
		put(OpcodeOut.SEND_PLAYER_COORDS, 255);
	}};

	@Override
	public PacketBuilder fromOpcodeEnum(OpcodeOut opcode, Player player) {
		PacketBuilder builder = null;
		Integer opcodeNum = opcodeMap.getOrDefault(opcode, null);
		if (opcodeNum != null) {
			builder = new PacketBuilder().setID(opcodeNum);
		}
		return builder;
	}

	@Override
	public Packet generate(AbstractStruct<OpcodeOut> payload, Player player) {

		PacketBuilder builder = fromOpcodeEnum(payload.getOpcode(), player);
		boolean possiblyValid = PayloadValidator.isPayloadCorrectInstance(payload, payload.getOpcode());

		if (builder != null && possiblyValid) {
			switch (payload.getOpcode()) {
				// no payload opcodes
				case SEND_APPEARANCE_CHANGE:
				case SEND_DEATH:
				case SEND_TRADE_CLOSE:
				case SEND_SHOP_CLOSE:
				case SEND_OPTIONS_MENU_CLOSE:
					break;

				case RUNESCAPE_UPDATED:
					// if a custom 2001scape is added these would be increased
					builder.writeByte((byte) 19); // verConfig
					builder.writeByte((byte) 14); // verMaps
					builder.writeByte((byte) 13); // verMedia
					builder.writeByte((byte) 6); // verModels
					builder.writeByte((byte) 5); // verTextures
					builder.writeByte((byte) 4); // verEntity
					break;

				case SEND_OPTIONS_MENU_OPEN:
					MenuOptionStruct mo = (MenuOptionStruct) payload;
					int numOptions = Math.min(mo.numOptions, 5);
					builder.writeByte((byte) numOptions);
					for (int i = 0; i < 5 && i < numOptions; i++){
						int optionLength = mo.optionTexts[i].length();
						builder.writeByte(optionLength);
						builder.writeNonTerminatedString(mo.optionTexts[i]);
					}
					break;

				case SEND_STATS:
					StatInfoStruct si = (StatInfoStruct) payload;
					for (int lvl : si.currentLevels)
						builder.writeByte((byte) lvl);
					for (int lvl : si.maxLevels)
						builder.writeByte((byte) lvl);
					break;

				case SEND_EQUIPMENT_STATS:
					EquipmentStatsStruct es = (EquipmentStatsStruct) payload;
					builder.writeByte((byte) es.armourPoints);
					builder.writeByte((byte) es.weaponAimPoints);
					builder.writeByte((byte) es.weaponPowerPoints);
					builder.writeByte((byte) es.magicPoints);
					builder.writeByte((byte) es.prayerPoints);
					builder.writeByte((byte) es.hidingPoints);
					break;

				case SEND_GAME_SETTINGS:
					GameSettingsStruct gs = (GameSettingsStruct) payload;
					builder.writeByte((byte) gs.playerKiller);
					builder.writeByte((byte) gs.cameraModeAuto);
					builder.writeByte((byte) gs.pkChangesLeft);
					builder.writeByte((byte) gs.mouseButtonOne);
					break;

				case SEND_PRIVACY_SETTINGS:
					PrivacySettingsStruct prs = (PrivacySettingsStruct) payload;
					builder.writeByte((byte) prs.hideStatus);
					builder.writeByte((byte) prs.blockChat);
					builder.writeByte((byte) prs.blockPrivate);
					builder.writeByte((byte) prs.blockTrade);
					builder.writeByte((byte) 0); // not implemented for mc38
					break;

				case SEND_TRADE_WINDOW:
					TradeShowWindowStruct tsw = (TradeShowWindowStruct) payload;
					builder.writeShort(tsw.serverIndex);
					break;

				case SEND_TRADE_ACCEPTED:
				case SEND_TRADE_OTHER_ACCEPTED:
					TradeAcceptStruct ta = (TradeAcceptStruct) payload;
					builder.writeByte((byte) ta.accepted);
					break;

				case SEND_TRADE_OTHER_ITEMS:
					TradeTransactionStruct tt = (TradeTransactionStruct) payload;
					int tradeCount = tt.opponentTradeCount;
					builder.writeByte((byte) tradeCount);
					for (int i = 0; i < tradeCount; i++) {
						builder.writeShort(tt.opponentCatalogIDs[i]);
						builder.writeShort(tt.opponentAmounts[i] & 0xffff);
					}
					break;

				case SEND_FRIEND_LIST:
					FriendListStruct fl = (FriendListStruct) payload;
					int friendSize = fl.listSize;
					builder.writeByte((byte) friendSize);
					for (int i = 0; i < friendSize; i++) {
						builder.writeLong(DataConversions.usernameToHash(fl.name[i]));
					}
					break;

				case SEND_FRIEND_UPDATE:
					FriendUpdateStruct fr = (FriendUpdateStruct) payload;
					builder.writeLong(DataConversions.usernameToHash(fr.name));
					builder.writeByte((byte) fr.onlineStatus);
					break;

				case SEND_IGNORE_LIST:
					IgnoreListStruct il = (IgnoreListStruct) payload;
					int ignoreSize = il.listSize;
					builder.writeByte((byte) ignoreSize);
					for (int i = 0; i < ignoreSize; i++) {
						builder.writeLong(DataConversions.usernameToHash(il.name[i]));
					}
					break;

				case SEND_INVENTORY:
					InventoryStruct is = (InventoryStruct) payload;
					int inventorySize = is.inventorySize;
					builder.writeByte((byte) inventorySize);
					for (int i = 0; i < inventorySize; i++) {
						builder.writeShort((is.wielded[i] << 15) | // First bit is if it is wielded or not
							is.catalogIDs[i]);
						builder.writeShort(is.amount[i] & 0xffff);
					}
					break;

				case SEND_SHOP_OPEN:
					ShopStruct s = (ShopStruct) payload;
					int shopSize = s.itemsStockSize;
					builder.writeByte((byte) shopSize);
					builder.writeByte((byte) s.isGeneralStore);
					builder.writeByte((byte) s.sellModifier);
					builder.writeByte((byte) s.buyModifier);
					builder.writeByte((byte) s.priceModifier);
					for (int i = 0; i < shopSize; i++) {
						builder.writeShort(s.catalogIDs[i]);
						builder.writeShort(s.amount[i]);
						builder.writeByte((byte) s.price[i]); //TODO: probably index of shop file
					}
					break;

				case SEND_SERVER_MESSAGE:
					MessageStruct m = (MessageStruct) payload;
					builder.writeNonTerminatedString(m.message);
					break;

				case SEND_PRIVATE_MESSAGE:
					PrivateMessageStruct pm = (PrivateMessageStruct) payload;
					builder.writeLong(DataConversions.usernameToHash(pm.playerName));
					builder.writeNonTerminatedString(pm.message);
					break;

				case SEND_WORLD_INFO:
					WorldInfoStruct wi = (WorldInfoStruct) payload;
					builder.writeShort(wi.serverIndex);
					builder.writeShort(wi.planeWidth);
					builder.writeShort(wi.planeHeight);
					builder.writeShort(wi.planeFloor);
					builder.writeShort(wi.distanceBetweenFloors);
					break;

				case SEND_NPC_COORDS:
				case SEND_PLAYER_COORDS:
					// TODO: CHECK IMPL
					break;

				case SEND_UPDATE_NPC: //VERIFIED
				case SEND_UPDATE_PLAYERS: //TYPE 1 changes!
				case SEND_UPDATE_PLAYERS_RETRO: //TYPE 5
					AppearanceUpdateStruct au = (AppearanceUpdateStruct) payload;
					for (Object entry : au.info) {
						if (entry instanceof Byte) {
							builder.writeByte((Byte) entry);
						} else if (entry instanceof Short) {
							builder.writeShort((Short) entry);
						} else if (entry instanceof Character) { // wrapper class for appearance byte
							int value = (Character) entry;
							builder.writeAppearanceByte((byte) value, 38);
						} else if (entry instanceof String) {
							builder.writeNonTerminatedString((String) entry);
						}
					}
					break;

				case SEND_SCENERY_HANDLER:
					GameObjectsUpdateStruct go = (GameObjectsUpdateStruct) payload;
					for (GameObjectLoc objectLoc : go.objects) {
						builder.writeShort(objectLoc.getId());
						builder.writeByte(objectLoc.getX());
						builder.writeByte(objectLoc.getY());
					}
					break;

				case SEND_BOUNDARY_HANDLER:
					GameObjectsUpdateStruct go1 = (GameObjectsUpdateStruct) payload;
					for (GameObjectLoc objectLoc : go1.objects) {
						builder.writeShort(objectLoc.getId());
						builder.writeByte(objectLoc.getX());
						builder.writeByte(objectLoc.getY());
						builder.writeByte(objectLoc.getDirection());
					}
					break;

				case SEND_GROUND_ITEM_HANDLER:
					GroundItemsUpdateStruct gri = (GroundItemsUpdateStruct) payload;
					for (ItemLoc it : gri.objects) {
						if (it.respawnTime == -1) {
							builder.writeByte(255);
						} else {
							builder.writeShort(it.getId());
						}
						builder.writeByte(it.getX());
						builder.writeByte(it.getY());
					}
					break;

				case SEND_REMOVE_WORLD_ENTITY:
					ClearLocationsStruct cl = (ClearLocationsStruct) payload;
					for (Point point : cl.points) {
						builder.writeShort(point.getX());
						builder.writeShort(point.getY());
					}
					break;

				case SEND_REMOVE_WORLD_NPC:
				case SEND_REMOVE_WORLD_PLAYER:
					ClearMobsStruct cm = (ClearMobsStruct) payload;
					for (Integer index : cm.indices) {
						builder.writeShort(index);
					}
					break;
			}
		}

		return builder != null ? builder.toPacket() : null;
	}
}
