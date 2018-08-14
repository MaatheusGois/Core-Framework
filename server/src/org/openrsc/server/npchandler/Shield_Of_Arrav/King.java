/**
* Generated By NPCScript :: A scripting engine created for openrsc by Zilent
*/

package org.openrsc.server.npchandler.Shield_Of_Arrav;

import org.openrsc.server.Config;
import org.openrsc.server.event.DelayedQuestChat;
import org.openrsc.server.logging.Logger;
import org.openrsc.server.logging.model.eventLog;
import org.openrsc.server.model.InvItem;
import org.openrsc.server.model.Npc;
import org.openrsc.server.model.Player;
import org.openrsc.server.model.Quest;
import org.openrsc.server.model.Quests;
import org.openrsc.server.model.World;
import org.openrsc.server.npchandler.NpcHandler;
import org.openrsc.server.util.DataConversions;
public class King implements NpcHandler {
	public void handleNpc(final Npc npc, final Player owner) throws Exception {
		npc.blockedBy(owner);
		owner.setBusy(true);
		Quest q = owner.getQuest(Quests.SHIELD_OF_ARRAV);
		if(q != null) {
			if(q.finished()) {
				noQuest(npc, owner);
			} else {
				switch(q.getStage()) {
					case 2:
					case 3:
						if(owner.getInventory().countId(61) > 0) {
							claimReward(npc, owner);
						} else {
							if(owner.getInventory().countId(53) > 0 && owner.getInventory().countId(54) > 0) {
								hasHalves(npc, owner);
							} else {
								noQuest(npc, owner);
							}
						}
						break;
					case 4:
						if(owner.getInventory().countId(61) > 0) {
							claimReward(npc, owner);
						} else {
							noQuest(npc, owner);
						}
						break;
					default:
						noQuest(npc, owner);
				}
			}
		} else {
			noQuest(npc, owner);
		}
	}
	
	private void claimReward(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Your majesty", "I have come to claim the reward", "for the return of the shield of Arrav"}, true) {
			public void finished() {
				owner.sendMessage("You show the certificate to the king");
				Quest blackarm = owner.getQuest(Quests.JOIN_BLACKARM_GANG);
				Quest phoenix = owner.getQuest(Quests.JOIN_PHOENIX_GANG);
				if(blackarm != null || phoenix != null) {
					World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"My goodness", "This is the claim for a reward put out by my father", "I never thought I'd see anyone claim this reward", "I see you are claiming half the reward", "So that would come to 600 gold coins"}) {
						public void finished() {
							owner.sendMessage("You hand over a certificate");
							owner.getInventory().remove(new InvItem(61, 1));
							owner.sendMessage("The king gives you 600 coins");
							owner.getInventory().add(new InvItem(10, 600));
							owner.sendInventory();
							owner.finishQuest(Quests.SHIELD_OF_ARRAV);
							Quest q = owner.getQuest(Quests.SHIELD_OF_ARRAV);
							owner.sendMessage("You have completed the Shield of Arrav quest");
							owner.sendMessage("@gre@You have gained " + q.getQuestPoints() + " quest points!");
							owner.setBusy(false);
							npc.unblock();
							Logger.log(new eventLog(owner.getUsernameHash(), owner.getAccount(), owner.getIP(), DataConversions.getTimeStamp(), "<strong>" + owner.getUsername() + "</strong>" + " has completed the <span class=\"recent_quest\">Shield of Arrav</span> quest!"));
						}
					});
				} else {
					World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"This isn't your name on the certificate", "I suggest you deliver it to whomever you stole it from."}) {
						public void finished() {
							owner.setBusy(false);
							npc.unblock();
						}
					});
				}

			}
		});
	}
	
	private void hasHalves(final Npc npc, final Player owner)  {
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Your majesty", "I have recovered the shield of Arrav", "I would like to claim the reward"}, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"The shield of Arrav, eh?", "I do recall my father putting a reward out for that", "Very well", "Go get the authenticity of the shield verified", "By the curator at the museum", "And I will grant you your reward"}) {
					public void finished() {
						owner.setBusy(false);
						npc.unblock();
					}
				});
			}
		});
	}
	
	private void noQuest(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Greetings, your majesty"}, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Do you have anything of import to say?"}) {
					public void finished() {
						World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Not really"}) {
							public void finished() {
								World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"You will have to excuse me then", "I am very busy", "I have a kingdom to run"}) {
									public void finished() {
										owner.setBusy(false);
										npc.unblock();
									}
								});
							}
						});
					}
				});
			}
		});
	}
}
