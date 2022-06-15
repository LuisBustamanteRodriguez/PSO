import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.domain.FIPANames;
import java.util.Vector;
import java.util.Enumeration;

@SuppressWarnings("unchecked")

public class Particula_Agent extends Agent {
    private int Responders;
    protected void setup() {
        // Leer los nombres de los respondedores como argumentos
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            Responders = args.length;
            System.out.println("PARTICULA: Tratando de delegar PSO a uno de "+ Responders +" respondedores.");
            // Rellene el mensaje de CFP
            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            for (int i = 0; i < args.length; ++i) {
                msg.addReceiver(new AID((String) args[i], AID.ISLOCALNAME));
            }
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
            // Queremos recibir una respuesta en 10 segundos
            msg.setContent("Realizar: metodo de optimizacion");
            addBehaviour(new ContractNetInitiator(this, msg) {
                protected void handlePropose(ACLMessage propose, Vector v) {
                    System.out.println("PARTICULA: Agente "+propose.getSender().getName()+" propuesta "+propose.getContent());
                }
                protected void handleRefuse(ACLMessage refuse) {
                    System.out.println("PARTICULA: Agente "+refuse.getSender().getName()+" rechazado");
                }
                protected void handleFailure(ACLMessage failure) {
                    if (failure.getSender().equals(myAgent.getAMS())) {
                        // Notificacion de FALLO del tiempo de ejecución de JADE: el receptor
                        System.out.println("PARTICULA: Respondedor no existe");
                    }
                    else {
                        System.out.println("PARTICULA: Agente "+failure.getSender().getName()+" error");
                    }
                    // no recibiremos respuesta de este agente
                    Responders--;
                }
                protected void handleAllResponses(Vector responses, Vector acceptances) {
                    if (responses.size() < Responders) {
                        // Salgun respondedor no respondio dentro del tiempo de espera especificado
                        System.out.println("PARTICULA: Tiempo de espera expirado: faltante "+(Responders - responses.size())+" respuestas");
                    }
                    // Evaluar propuestas.
                    String bestProposal = "";
                    AID bestProposer = null;
                    ACLMessage accept = null;
                    Enumeration e = responses.elements();
                    while (e.hasMoreElements()) {
                        ACLMessage msg = (ACLMessage) e.nextElement();
                        if (msg.getPerformative() == ACLMessage.PROPOSE) {
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                            acceptances.addElement(reply);
                            String proposal = String.valueOf(msg.getContent());
                            System.out.println("la propuesta es: "+proposal);
                            if (proposal !="PSO METODO") {
                                bestProposal = proposal;
                                bestProposer = msg.getSender();
                                accept = reply;
                            }
                        }
                    }
                    // Aceptar la propuesta del mejor proponente
                    if (accept != null) {
                        System.out.println("Aceptando propuesta "+bestProposal+" del respondedor "+bestProposer.getName());
                        accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    }
                }
                protected void handleInform(ACLMessage inform) {
                    System.out.println("PSO: Agente "+inform.getSender().getName()+" realizo con exito la accion solicitada");
                }
            } );
        }
        else {
            System.out.println("No se especifico ningun respondedor.");
        }
    }
}
