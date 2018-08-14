/**
* Generated By NPCScript :: A scripting engine created for openrsc by Zilent
*/
package org.openrsc.server.npchandler.The_Restless_Ghost;

import org.openrsc.server.Config;
import org.openrsc.server.event.SingleEvent;
import org.openrsc.server.model.Npc;
import org.openrsc.server.model.ChatMessage;
import org.openrsc.server.model.MenuHandler;
import org.openrsc.server.model.World;
import org.openrsc.server.event.DelayedQuestChat;
import org.openrsc.server.model.InvItem;
import org.openrsc.server.model.Player;
import org.openrsc.server.model.Quest;
import org.openrsc.server.model.Quests;
import org.openrsc.server.npchandler.NpcHandler;
public class Urhney implements NpcHandler {

	public void handleNpc(final Npc npc, final Player owner) throws Exception {
		owner.setBusy(true);
		npc.blockedBy(owner);
		final String[] messages44 = {"Go away, I'm meditating"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages44, true) {
			public void finished() {
				final Quest q = owner.getQuest(Quests.THE_RESTLESS_GHOST);
				if(q != null) {
					if(q.finished()) { //Quest Finished
						questNotStarted(npc, owner);
					} else {//Quest Underway
						switch(q.getStage()) {
							case 0:
								World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
									public void action() {
										final String[] options12 = {"Father Aereck sent me to talk to you", "Well that's friendly", "I've come to repossess your house"};
										owner.setBusy(false);
										owner.sendMenu(options12);
										owner.setMenuHandler(new MenuHandler(options12) {
											public void handleReply(final int option, final String reply) {
												owner.setBusy(true);
												for(Player informee : owner.getViewArea().getPlayersInView()) {
													informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
												}
												switch(option) {
													case 0:
														fatherAereck(npc, owner);
														break;
													case 1:
														goAway(npc, owner);
														break;
													case 2:
														repossess(npc, owner);
														break;
												}
											}
										});
									}
								});
								break;
							default:
								if(owner.getInventory().contains(24)) {
									questNotStarted(npc, owner);
								} else {
									lostAmulet(npc, owner);
								}
						}
					}
				} else { //Quest Not Started
					questNotStarted(npc, owner);
				}
			}
		});
	}
	
	private void goAway(final Npc npc, final Player owner) {
		final String[] messages46 = {"I said go away!"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages46) {
			public void finished() {
				final String[] messages47 = {"Ok, ok"};
				World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages47) {
					public void finished() {
						owner.setBusy(false);
						npc.unblock();
					}
				});
			}
		});
	}
	
	private void fatherAereck(final Npc npc, final Player owner) {
		final String[] messages45 = {"I suppose I'd better talk to you then", "What problems has he got himself into this time?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages45) {
			public void finished() {
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
					public void action() {
						final String[] options13 = {"He's got a ghost haunting his graveyard", "You mean he gets himself into lots of problems?"};
						owner.setBusy(false);
						owner.sendMenu(options13);
						owner.setMenuHandler(new MenuHandler(options13) {
							public void handleReply(final int option, final String reply) {
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView()) {
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) {
									case 0:
										ghost(npc, owner);
										break;
									case 1:
										problems(npc, owner);
										break;
								}
							}
						});
					}
				});
			}
		});
	}
	
	private void lostAmulet(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
			public void action() {
				final String[] options12 = {"I've lost the amulet", "Well that's friendly", "I've come to repossess your house"};
				owner.setBusy(false);
				owner.sendMenu(options12);
				owner.setMenuHandler(new MenuHandler(options12) {
					public void handleReply(final int option, final String reply) {
						owner.setBusy(true);
						for(Player informee : owner.getViewArea().getPlayersInView()) {
							informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
						}
						switch(option) {
							case 0:
								World.getDelayedEventHandler().add(new SingleEvent(owner, 1000) {
									public void action() {
										owner.sendMessage("Father Urhney sighs");
										final String[] messages0 = {"How careless can you get", "Those things aren't easy to come by you know", "It's a good job I've got a spare", "Be more careful this time"};
										World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages0, true) {
											public void finished() {
												owner.sendMessage("Father Urhney hands you an amulet");
												owner.getInventory().add(new InvItem(24, 1));
												owner.sendInventory();
												final String[] messages1 = {"Ok I'll try to be"};
												World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages1) {
													public void finished() {
														owner.setBusy(false);
														npc.unblock();
													}
												});
											}
										});
									}
								});
								break;
							case 1:
								final String[] messages46 = {"I said go away!"};
								World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages46) {
									public void finished() {
										final String[] messages47 = {"Ok, ok"};
										World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages47) {
											public void finished() {
												owner.setBusy(false);
												npc.unblock();
											}
										});
									}
								});
								break;
							case 2:
								repossess(npc, owner);
								break;
						}
					}
				});
			}
		});
	}
	
	private void questNotStarted(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
			public void action() {
				final String[] options12 = {"Well that's friendly", "I've come to repossess your house"};
				owner.setBusy(false);
				owner.sendMenu(options12);
				owner.setMenuHandler(new MenuHandler(options12) {
					public void handleReply(final int option, final String reply) {
						owner.setBusy(true);
						for(Player informee : owner.getViewArea().getPlayersInView()) {
							informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
						}
						switch(option) {
							case 0:
								final String[] messages46 = {"I said go away!"};
								World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages46) {
									public void finished() {
										final String[] messages47 = {"Ok, ok"};
										World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages47) {
											public void finished() {
												owner.setBusy(false);
												npc.unblock();
											}
										});
									}
								});
								break;
							case 1:
								repossess(npc, owner);
								break;
						}
					}
				});
			}
		});
	}
	
	private void repossess(final Npc npc, final Player owner) {
		final String[] messages48 = {"Under what grounds?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages48) {
			public void finished() {
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
					public void action() {
						final String[] options14 = {"Repeated failure on mortgage payments", "I don't know, I just wanted this house"};
						owner.setBusy(false);
						owner.sendMenu(options14);
						owner.setMenuHandler(new MenuHandler(options14) {
							public void handleReply(final int option, final String reply) {
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView()) {
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) {
									case 0:
										final String[] messages49 = {"I don't have a mortgage", "I built this house myself"};
										World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages49) {
											public void finished() {
												final String[] messages50 = {"Sorry I must have the wrong address", "All the houses look the same around here"};
												World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages50) {
													public void finished() {
														owner.setBusy(false);
														npc.unblock();
													}
												});
											}
										});
										break;
									case 1:
										final String[] messages51 = {"Oh go away and stop wasting my time"};
										World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages51) {
											public void finished() {
												owner.setBusy(false);
												npc.unblock();
											}
										});
										break;
								}
							}
						});
					}
				});
			}
		});
	}
	
	private void problems(final Npc npc, final Player owner) {
		final String[] messages52 = {"Yeah, for example when we were trainee priests", "He kept on getting stuck up bell ropes", "Anyway I don't have time for chitchat", "What's his problem this time?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages52) {
			public void finished() {
				ghost(npc, owner);
			}
		});
	}
	
	private void ghost(final Npc npc, final Player owner) {
		final String[] messages53 = {"Oh the silly fool", "I leave town for just five months", "and already he can't manage", "Sigh", "Well I can't go back and exorcise it", "I vowed not to leave this place", "Until I had done a full two years of prayer and meditation", "Tell you what I can do though", "Take this amulet"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages53) {
			public void finished() {
				owner.sendMessage("Father Urhney hands you an amulet");
				owner.getInventory().add(new InvItem(24, 1));
				owner.sendInventory();
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"It is an amulet of Ghostspeak", "So called because when you wear it you can speak to ghosts", "A lot of ghosts are doomed to be ghosts", "Because they have left some task uncompleted", "Maybe if you know what this task is", "You can get rid of the ghost", "I'm not making any guarantees mind you", "But it is the best I can do right now"}) {
					public void finished() {
						final String[] messages54 = {"Thank you, I'll give it a try"};
						World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages54) {
							public void finished() {
								owner.incQuestCompletionStage(Quests.THE_RESTLESS_GHOST);
								owner.setBusy(false);
								npc.unblock();
							}
						});
					}
				});
			}
		});
	}
}