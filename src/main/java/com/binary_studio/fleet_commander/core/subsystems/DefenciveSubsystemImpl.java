package com.binary_studio.fleet_commander.core.subsystems;

import com.binary_studio.fleet_commander.core.actions.attack.AttackAction;
import com.binary_studio.fleet_commander.core.actions.defence.RegenerateAction;
import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.subsystems.contract.DefenciveSubsystem;

public final class DefenciveSubsystemImpl implements DefenciveSubsystem {

	private final String name;

	private final PositiveInteger powergridConsumption;

	private final PositiveInteger capacitorConsumption;

	private final PositiveInteger impactReductionPercent;

	private final PositiveInteger shieldRegeneration;

	private final PositiveInteger hullRegeneration;

	private DefenciveSubsystemImpl(String name, PositiveInteger powergridConsumption,
			PositiveInteger capacitorConsumption, PositiveInteger impactReductionPercent,
			PositiveInteger shieldRegeneration, PositiveInteger hullRegeneration) {

		this.name = name;
		this.powergridConsumption = powergridConsumption;
		this.capacitorConsumption = capacitorConsumption;
		this.impactReductionPercent = impactReductionPercent;
		this.shieldRegeneration = shieldRegeneration;
		this.hullRegeneration = hullRegeneration;
	}

	public static DefenciveSubsystemImpl construct(String name, PositiveInteger powergridConsumption,
			PositiveInteger capacitorConsumption, PositiveInteger impactReductionPercent,
			PositiveInteger shieldRegeneration, PositiveInteger hullRegeneration) throws IllegalArgumentException {

		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Name should be not null and not empty");
		}

		var reductionPercent = impactReductionPercent;
		if (impactReductionPercent.value() > 95) {
			reductionPercent = PositiveInteger.of(95);
		}

		return new DefenciveSubsystemImpl(name, powergridConsumption, capacitorConsumption, reductionPercent,
				shieldRegeneration, hullRegeneration);
	}

	@Override
	public PositiveInteger getPowerGridConsumption() {
		return this.powergridConsumption;
	}

	@Override
	public PositiveInteger getCapacitorConsumption() {
		return this.capacitorConsumption;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public AttackAction reduceDamage(AttackAction incomingDamage) {

		Integer damage = incomingDamage.damage.value();
		Integer reductionPercent = this.impactReductionPercent.value();

		int reducedDamage = (int) Math.ceil(damage * (1 - reductionPercent / 100.));

		return new AttackAction(PositiveInteger.of(reducedDamage), incomingDamage.attacker, incomingDamage.target,
				incomingDamage.weapon);
	}

	@Override
	public RegenerateAction regenerate() {
		return new RegenerateAction(this.shieldRegeneration, this.hullRegeneration);
	}

}
