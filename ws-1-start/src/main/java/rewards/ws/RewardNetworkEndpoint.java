package rewards.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;
import rewards.ws.types.RewardAccountForDiningRequest;
import rewards.ws.types.RewardAccountForDiningResponse;

@Endpoint
public class RewardNetworkEndpoint {

	private static final String NAMESPACE_URI = "http://www.springsource.com/reward-network";

	private RewardNetwork rewardNetwork;

	@Autowired
	public RewardNetworkEndpoint(RewardNetwork rewardNetwork) {
		this.rewardNetwork = rewardNetwork;
	}
	
	// TODO 3: DONE Create a new method which accepts a RewardAccountForDiningRequest
	// as unmarshalled request payload, processes the request and returns
	// a RewardAccountForDiningResponse as the response payload to be marshalled
	// for the appropriate payload root element. 

	@PayloadRoot(localPart="rewardAccountForDiningRequest",
				 namespace="http://www.springsource.com/reward-network")
	@ResponsePayload
	public RewardAccountForDiningResponse getRewardAccountForDiningResponse(@RequestPayload RewardAccountForDiningRequest request){
		
		RewardConfirmation rewardConfirmation;
		if (null != request)
		{			
			 rewardConfirmation = 
					rewardNetwork.rewardAccountFor(Dining.createDining(request.getAmount().toString(), request.getCreditCardNumber(), request.getMerchantNumber()));
			 RewardAccountForDiningResponse rewardAccountForDiningResponse = new RewardAccountForDiningResponse();
			 AccountContribution accountContribution = rewardConfirmation.getAccountContribution();
			 
			 if (null != accountContribution)
			 {
				 rewardAccountForDiningResponse.setAccountNumber(accountContribution.getAccountNumber());
				 rewardAccountForDiningResponse.setAmount(accountContribution.getAmount().asBigDecimal());
				 rewardAccountForDiningResponse.setConfirmationNumber(rewardConfirmation.getConfirmationNumber());
			 }

			 return rewardAccountForDiningResponse;
		}
		throw new IllegalArgumentException("Request is null");
	}

}