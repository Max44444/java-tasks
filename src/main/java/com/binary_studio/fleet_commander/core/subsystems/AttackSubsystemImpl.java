package com.binary_studio.fleet_commander.core.subsystems;

import com.binary_studio.fleet_commander.core.common.Attackable;
import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.subsystems.contract.AttackSubsystem;

public final class AttackSubsystemImpl implements AttackSubsystem {

	private final String name;

	private final PositiveInteger powergridRequirments;

	private final PositiveInteger capacitorConsumption;

	private final PositiveInteger optimalSpeed;

	private final PositiveInteger optimalSize;

	private final PositiveInteger baseDamage;

	private AttackSubsystemImpl(String name, PositiveInteger powergridRequirments, PositiveInteger capacitorConsumption,
			PositiveInteger optimalSpeed, PositiveInteger optimalSize, PositiveInteger baseDamage) {
		this.name = name;
		this.powergridRequirments = powergridRequirments;
		this.capacitorConsumption = capacitorConsumption;
		this.optimalSpeed = optimalSpeed;
		this.optimalSize = optimalSize;
		this.baseDamage = baseDamage;
	}

	public static AttackSubsystemImpl construct(String name, PositiveInteger powergridRequirments,
			PositiveInteger capacitorConsumption, PositiveInteger optimalSpeed, PositiveInteger optimalSize,
			PositiveInteger baseDamage) throws IllegalArgumentException {

		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Name should be not null and not empty");
		}

		return new AttackSubsystemImpl(name, powergridRequirments, capacitorConsumption, optimalSpeed, optimalSize,
				baseDamage);
	}

	@Override
	public PositiveInteger getPowerGridConsumption() {
		return this.powergridRequirments;
	}

	@Override
	public PositiveInteger getCapacitorConsumption() {
		return this.capacitorConsumption;
	}

	@Override
	public PositiveInteger attack(Attackable target) {
		Integer targetSize = target.getSize().value();
		Integer targetSpeed = target.getCurrentSpeed().value();

		Integer optimalSize = this.optimalSize.value();
		Integer optimalSpeed = this.optimalSpeed.value();

		double sizeReductionModifier = targetSize >= optimalSize ? 1 : (double) targetSize / optimalSize;
		double speedReductionModifier = (targetSpeed <= optimalSpeed) ? 1 : (double) optimalSpeed / (2 * targetSpeed);

		double damage = Math.ceil(this.baseDamage.value() * Math.min(sizeReductionModifier, speedReductionModifier));

		return PositiveInteger.of((int) damage);
	}

	@Override
	public String getName() {
		return this.name;
	}

}
