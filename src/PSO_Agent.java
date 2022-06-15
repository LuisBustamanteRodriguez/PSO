import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.FailureException;
import java.text.DecimalFormat;

//javac -cp lib\jade.jar C:\Users\luisi\IdeaProjects\PSO\src\*.java -d classes\
//java -cp lib\jade.jar;classes\ jade.Boot -gui PSO-AGENT:PSO_Agent

@SuppressWarnings("unchecked")
public class PSO_Agent extends Agent {

    protected void setup() {
        System.out.println("PSO: Agente "+getLocalName()+" esperando la PPC!");
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP) );

        addBehaviour(new ContractNetResponder(this, template) {
            @Override
            protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
                System.out.println("PSO: Agente "+getLocalName()+": CFP recibido de "+cfp.getSender().getName()+". acccion es "+cfp.getContent());
                String proposal = evaluate();
                // Brindamos una propuesta
                System.out.println("PSO: Agent "+getLocalName()+": proponiendo "+proposal);
                ACLMessage propose = cfp.createReply();
                propose.setPerformative(ACLMessage.PROPOSE);
                propose.setContent(String.valueOf(proposal));
                return propose;
            }
            @Override
            protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose,ACLMessage accept) throws FailureException {
                System.out.println("PSO: Agente "+getLocalName()+": Propuesta aceptada!");
                System.out.println("Calculo de iteraciones...");
                perform();
                System.out.println("PSO: Agente "+getLocalName()+": Accion realizada con exito!");
                ACLMessage inform = accept.createReply();
                inform.setPerformative(ACLMessage.INFORM);
                return inform;
            }
            protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
                System.out.println("PSO: Agente "+getLocalName()+": Propuesta rechazada!!!");
            }
        } );
    }

    private String evaluate() {
        // Simular una evaluacion generando un numero aleatorio
        String str = "PSO METODO";
        return str;
    }

    private void perform() {
        DecimalFormat df = new DecimalFormat("#.###");
        Metodos met = new Metodos();
        DataSet ds = new DataSet();
        int t=0;

        do{
            if(t==0){
                //PASO 1
                ds.setX_val(met.initPopulation());
                ds.setVel(met.IniciVel());
                ds.setX_val(met.initPosition( ds.getX_val(), ds.getVel()) );
                ds.setPersonalBest(met.iniPersoB(ds.getX_val()));
                //PASO 2
                ds.setFitness(met.FitnessFuntion(ds.getX_val()));
                ds.setMinFitnessValue(met.fitnessValue(ds.getFitness()));
                ds.setGlobalBest(met.GlobalBest(ds.getFitness(), ds.getMinFitnessValue(),ds.getX_val()));
            }else{
                //PASO 3
                ds.setVel(met.updateVel(ds.getVel(), ds.getX_val(), ds.getPersonalBest(), ds.getGlobalBest()));
                System.out.println("MATRIZ DE VELOCIDAD:");
                met.Matrix(ds.getVel());
                ds.setX_val(met.updatePos(ds.getVel(), ds.getX_val()));
                System.out.println("MATRIZ DE POSICION:");
                met.Matrix(ds.getX_val());
                //STEP 4
                ds.setMinFitnessValue(met.updateFitnessVal(ds.getMinFitnessValue(), met.FitnessFuntion(ds.getX_val())));
                ds.setGlobalBest(met.GlobalBest(met.FitnessFuntion(ds.getX_val()), ds.getMinFitnessValue(), ds.getX_val()));
                ds.setPersonalBest(met.updatePersoBest(ds.getX_val(), ds.getPersonalBest(), ds.getFitness(), met.FitnessFuntion(ds.getX_val())));
            }
            //PASO 5
            t++;
            //PASO 6
            System.out.println("\nMejor particula: "); met.Array(ds.getGlobalBest());
            System.out.println("Global best: "+df.format(ds.getMinFitnessValue()));
        }while(t<met.itera);
    }
}
