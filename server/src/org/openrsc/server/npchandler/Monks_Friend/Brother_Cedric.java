/**
* Generated By NPCScript :: A scripting engine created for openrsc by Zilent
*/

//scripted by Mr. Zain

package org.openrsc.server.npchandler.Monks_Friend;
import org.openrsc.server.Config;
import org.openrsc.server.event.DelayedQuestChat;
import org.openrsc.server.event.SingleEvent;
import org.openrsc.server.model.ChatMessage;
import org.openrsc.server.model.MenuHandler;
import org.openrsc.server.model.Npc;
import org.openrsc.server.model.Player;
import org.openrsc.server.model.Quest;
import org.openrsc.server.model.Quests;
import org.openrsc.server.model.World;
import org.openrsc.server.npchandler.NpcHandler;



public class Brother_Cedric implements NpcHandler {

	public void handleNpc(final Npc npc, final Player owner) throws Exception {
		npc.blockedBy(owner);
		owner.setBusy(true);
		Quest q = owner.getQuest(Quests.MONKS_FRIEND);
		if(q != null) {
			if(q.finished()) {
				finished(npc, owner);
			} else {
				switch(q.getStage()) {
					case 0:
						noQuestStarted(npc, owner);
						break;
					case 3:
						questPart2Stage1(npc, owner);
						break;
					case 4:
						questPart2Stage2(npc, owner);
						break;
					case 5:
						questPart2Stage3(npc, owner);
						break;
					case 6:
						questPart2Stage4(npc, owner);
					break;
					default:
						noQuestStarted(npc, owner);
				}
			}
		} else {
			noQuestStarted(npc, owner);
		}
	}
	

	private void noQuestStarted(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Hello"}, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Honey, money, woman, wine.."}) {
					public void finished() {
						World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Are you ok?"}) {
							public void finished() {
								World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Yesshh...hic up...beautiful.."}) {
									public void finished() {
										World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Take care old monk"}) {
											public void finished() {
												World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Ia..di..da..hic..up"}) {
													public void finished() {
														owner.sendMessage("The monk has had too much to drink");
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
				});
			}
		});
	}
	
	private void questPart2Stage1(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Brother Cedric are you okay?"}, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Yeesshhh, I'm very, very....", "..drunk..hic..up.."}) {
					public void finished() {
						World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Brother Omad needs the wine..", ".. for the party"}) {
							public void finished() {
								World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Oh dear, oh dear", "I knew I had to do something", "Pleashhh, find me some water", "Once I'm sober I'll help you..", "..take the wine back."}) {
									public void finished() {
										owner.incQuestCompletionStage(Quests.MONKS_FRIEND);
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
	
	private void questPart2Stage2(final Npc npc, final Player owner) {
		if(owner.getInventory().countId(50) < 1) {
			World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Are you okay?"}, true) {
				public void finished() {
					World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"...hic up..oh my head..", "..I need some water."}) {
						public void finished() {
							owner.setBusy(false);
							npc.unblock();	
						}
					});
				}
			});
		}
		else
		if(owner.getInventory().countId(50) > 0) {
			World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Are you okay?"}, true) {
				public void finished() {
					World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"...hic up..oh my head..", "..I need some water."}) {
						public void finished() {
							World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Cedric, here, drink some water"}) {
								public void finished() {
									World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Oh yes, my head's starting to spin", "gulp...gulp"}) {
										public void finished() {
											owner.getInventory().remove(50, 1);
											owner.sendInventory();
											owner.sendMessage("Brother Cedric drinks the water");
											World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"aah, that's better"}) {
												public void finished() {
													owner.incQuestCompletionStage(Quests.MONKS_FRIEND);
													owner.sendMessage("You throw the excess water over brother Cedric");
													World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Now I just need to fix this cart..", "..and we can go party", "Could you help?"}) {
														public void finished() {
															World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
																public void action() {
																	final String[] options107 = {"No, I've helped enough monks today", "Yes I'd be happy to"};
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
																					owner.setBusy(false);
																					npc.unblock();
																					break;
																				case 1:
																					wood(npc, owner);
																				break;
																			}
																		}
																	});
																}
															});
														}
													});
												}
											});
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
	
	
	private void questPart2Stage3(final Npc npc, final Player owner) {
		if(owner.getInventory().countId(14) < 1) {
			World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"I need some wood for this cart"}, true) {
				public void finished() {
					World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"I'll go cut some then"}) {
						public void finished() {
						owner.setBusy(false);
						npc.unblock();
						}
					});
				}
			});
		}
		else
		if(owner.getInventory().countId(14) > 0) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"I need some wood for this cart"}, true) {
				public void finished() {
					World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Here you go..", "I've got some wood"}) {
						public void finished() {
							owner.getInventory().remove(14, 1);
							owner.sendInventory();
							owner.incQuestCompletionStage(Quests.MONKS_FRIEND);
							World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Well done, now I'll fix this cart", "You head back to Brother Omad", "Tell him I'm on my way", "I won't be long"}) {
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
	}
	
	private void questPart2Stage4(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"You head back to Brother Omad", "Tell him I'm on my way", "I won't be long"}, true) {
			public void finished() {
				owner.setBusy(false);
				npc.unblock();
			}
		});
	}
	
	private void wood(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"I need some wood for this cart"}) {
			public void finished() {
				World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"I'll go cut some then"}) {
					public void finished() {
					owner.setBusy(false);
					npc.unblock();
					}
				});
			}
		});
	}
	
	private void finished(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Hic..Hello again", "shhank you fer your help"}, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"No problem"}) {
					public void finished() {
					owner.setBusy(false);
					npc.unblock();
					}
				});
			}
		});
	}

	
}