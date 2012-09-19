package rewards.internal;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import rewards.RewardNetwork;
import rewards.internal.account.AccountRepository;
import rewards.internal.account.JdbcAccountRepository;
import rewards.internal.restaurant.JdbcRestaurantRepository;
import rewards.internal.restaurant.RestaurantRepository;
import rewards.internal.reward.JdbcRewardRepository;
import rewards.internal.reward.RewardRepository;

@Configuration
public class ApplicationConfig {

	@Autowired
	DataSource dataSource;

	@Bean
	RewardRepository rewardRepository() 
	{	
		return new JdbcRewardRepository(dataSource);
	}

	@Bean
	RestaurantRepository restaurantRepository() 
	{	
		return new JdbcRestaurantRepository(dataSource);
	}

	@Bean
	AccountRepository accountRepository() 
	{		
		return new JdbcAccountRepository(dataSource);
	}

	@Bean
	RewardNetwork rewardNetwork()
	{
		return new RewardNetworkImpl(accountRepository(), restaurantRepository(), rewardRepository());
	}
}
