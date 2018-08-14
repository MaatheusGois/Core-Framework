/**
* Generated By NPCScript :: A scripting engine created for openrsc by Zilent
*/

//scripted by Mr. Zain

package org.openrsc.server.npchandler.Fishing_Contest;
import org.openrsc.server.Config;
import org.openrsc.server.event.DelayedQuestChat;
import org.openrsc.server.event.ShortEvent;
import org.openrsc.server.event.SingleEvent;
import org.openrsc.server.model.ChatMessage;
import org.openrsc.server.model.MenuHandler;
import org.openrsc.server.model.Npc;
import org.openrsc.server.model.Player;
import org.openrsc.server.model.Quest;
import org.openrsc.server.model.Quests;
import org.openrsc.server.model.World;
import org.openrsc.server.npchandler.NpcHandler;



public class Bonzo implements NpcHandler {

	public void handleNpc(final Npc npc, final Player owner) throws Exception {
		npc.blockedBy(owner);
		owner.setBusy(true);
		Quest q = owner.getQuest(Quests.FISHING_CONTEST);
		if(q != null) {
			if(q.finished()) {
				finished(npc, owner);
			} else {
				switch(q.getStage()) {
					case 0:
						noQuestStarted(npc, owner);
						break;
					case 1:
						questStage1(npc, owner);
						break;
					case 2:
						questStage2(npc, owner);
						break;
					case 3:
						questStage3(npc, owner);
						break;
					case 4:
						questStage4(npc, owner);
						break;
				}
			}
		} else {
			noQuestStarted(npc, owner);
		}
	}
	
	
	private void noQuestStarted(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Only people with passes are allowed in here", "Get out"}, true) {
			public void finished() {
				owner.teleport(560, 492, false);
				owner.setBusy(false);
				npc.unblock();
			}
		});
	}
	
	
	private void questStage1(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Roll up, roll up!", "Enter the great Hemenster fishing competition", "Only 5gp entrance fee"}, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
					public void action() {
						final String[] options107 = {"I'll give that a go then", "No thanks, I'll just watch the fun"};
						owner.setBusy(false);
						owner.sendMenu(options107);
						owner.setMenuHandler(new MenuHandler(options107) {
							public void handleReply(final int option, final String reply) {
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView()) {
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) {
									case 0:
										if(owner.getInventory().countId(10) > 4) {
										acceptedEnroll1(npc, owner);
										}
										else
										{
										declinedEnroll(npc, owner);
										}
										break;
									case 1:
										owner.setBusy(false);
										npc.unblock();
										break;
								}
							}
						});
					}
				});
			}
		});
	}
	
	
	private void questStage2(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Roll up, roll up!", "Enter the great Hemenster fishing competition", "Only 5gp entrance fee"}, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
					public void action() {
						final String[] options107 = {"I'll give that a go then", "No thanks, I'll just watch the fun"};
						owner.setBusy(false);
						owner.sendMenu(options107);
						owner.setMenuHandler(new MenuHandler(options107) {
							public void handleReply(final int option, final String reply) {
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView()) {
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) {
									case 0:
										if(owner.getInventory().countId(10) > 4) {
										acceptedEnroll2(npc, owner);
										}
										else
										{
										declinedEnroll(npc, owner);
										}
										break;
									case 1:
										owner.setBusy(false);
										npc.unblock();
										break;
								}
							}
						});
					}
				});
			}
		});	
	}
	
	
	private void questStage3(final Npc npc, final Player owner) {
		if(owner.getInventory().countId(717) < 1) {
			World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"So how are you doing so far?"}, true) {
				public void finished() {
					World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"I haven't caught anything yet"}) {
						public void finished() {
							World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Well get fishing, time is almost up"}) {
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
		else
		if(owner.getInventory().countId(717) > 0) {
			World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"So how are you doing so far?"}, true) {
				public void finished() {
					World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
						public void action() {
							final String[] options107 = {"I have this big fish, is it big enough to win?", "I think I might still be able to find a bigger fish"};
							owner.setBusy(false);
							owner.sendMenu(options107);
							owner.setMenuHandler(new MenuHandler(options107) {
								public void handleReply(final int option, final String reply) {
									owner.setBusy(true);
									for(Player informee : owner.getViewArea().getPlayersInView()) {
										informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
									}
									switch(option) {
										case 0:
											fishCaught(npc, owner);
											break;
										case 1:
											owner.setBusy(false);
											npc.unblock();
											break;
									}
								}
							});
						}
					});
				}
			});	
		}
	}
	
	
	private void questStage4(final Npc npc, final Player owner) {
		if(owner.getInventory().countId(720) < 1) {
			World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"I seem to have lost my trophy"}, true) {
				public void finished() {
					World.getDelayedEventHandler().add(new DelayedQuestChat(npc, npc, new String[] {"Here you are"}) {
						public void finished() {
							owner.getInventory().add(720, 1);
							owner.sendInventory();
							owner.sendMessage("Bonzo gives you another trophy");							
							owner.setBusy(false);
							npc.unblock();
						}
					});
				}
			});
		}
		else
		if(owner.getInventory().countId(720) > 0) {
			World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Congratulations on winning the Hemenster fishing trophy"}, true) {
				public void finished() {
					World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Thank you"}) {
						public void finished() {
							owner.setBusy(false);
							npc.unblock();
						}
					});
				}
			});
		}
	}
	

	private void acceptedEnroll1(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Marvellous", "You need to pay 5gp to enter"}) {
			public void finished() {
				owner.getInventory().remove(10, 5);
				owner.sendInventory();
				owner.sendMessage("You pay Bonzo 5 coins");
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Ok we've got all the fishermen", "It's time to roll", "Ok nearly everyone is in their place already", "You fish in the spot by the oak tree", "And the sinister stranger you fish by the pipes"}) {
					public void finished() {
						World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Okay by the oak tree it is"}) {
							public void finished() {	
								owner.sendMessage("You start fishing...");
								owner.sendMessage("...but catch nothing");
								World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Ooh, better luck next time buddy"}) {
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
	
	
	private void acceptedEnroll2(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Marvellous", "You need to pay 5gp to enter"}) {
			public void finished() {
				owner.getInventory().remove(10, 5);
				owner.sendInventory();
				owner.sendMessage("You pay Bonzo 5 coins");
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Ok we've got all the fishermen", "It's time to roll", "Ok nearly everyone is in their place already", "You fish in the spot by the oak tree", "And the sinister stranger you fish by the pipes"}) {
					public void finished() {
						Npc Sinister_Stranger = World.getNpc(346, 567, 570, 491, 494);
						if (Sinister_Stranger != null) 
						{
							for(Player informee : Sinister_Stranger.getViewArea().getPlayersInView()) 
							{
								informee.informOfNpcMessage(new ChatMessage(Sinister_Stranger, "arrgh what is that ghastly smell", owner));
								World.getDelayedEventHandler().add(new ShortEvent(owner) 
								{
									public void action()
									{
										Npc Sinister_Stranger = World.getNpc(346, 567, 570, 491, 494);
										for(Player informee : Sinister_Stranger.getViewArea().getPlayersInView()) 
										{
										informee.informOfNpcMessage(new ChatMessage(Sinister_Stranger, "I think I will move over here instead", owner));
										}
									}
								});
							}							
						}
						World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Hmm you'd better go and take the area by the pipes then"}) {
							public void finished() {
								owner.incQuestCompletionStage(Quests.FISHING_CONTEST);
								owner.sendMessage("Your fishing competition spot is beside the pipes");
								owner.setBusy(false);
								npc.unblock();	
							}
						});
					}
				});
			}
		});
	}
	
	
	private void fishCaught(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Well we'll just wait till time is up"}) {
			public void finished() {
				owner.sendMessage("You wait..");
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Okay folks times up", "Lets see who caught the biggest fish"}) {
					public void finished() {
						owner.getInventory().remove(717, 1);
						owner.sendInventory();
						owner.sendMessage("You hand over your catch");
						World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"We have a new winner", "The heroic looking person", "Who was fishing by the pipes", "Has caught the biggest carp", "I've seen since Grandpa Jack used to compete"}) {
							public void finished() {
								owner.getInventory().add(720, 1);
								owner.sendInventory();
								owner.incQuestCompletionStage(Quests.FISHING_CONTEST);
								owner.sendMessage("You are given the Hemenster fishing trophy");
								owner.setBusy(false);
								npc.unblock();
							}	
						});		
					}
				});
			}
		});
	}
	
	
	private void declinedEnroll(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Sorry mate", "You need to pay 5gp to enter"}) {
			public void finished() {
				World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Ok, I'll go get some money then"}) {
					public void finished() {
						owner.setBusy(false);
						npc.unblock();
					}
				});
			}
		});
	}
	
	
	private void finished(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Congratulations on winning the Hemenster fishing trophy"}, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Thank you"}) {
					public void finished() {
						owner.setBusy(false);
						npc.unblock();
					}
				});
			}
		});
	}
	
	
}