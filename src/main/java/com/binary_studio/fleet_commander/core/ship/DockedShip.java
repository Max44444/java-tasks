package com.binary_studio.fleet_commander.core.ship;

import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.exceptions.InsufficientPowergridException;
import com.binary_studio.fleet_commander.core.exceptions.NotAllSubsystemsFitted;
import com.binary_studio.fleet_commander.core.ship.contract.ModularVessel;
import com.binary_studio.fleet_commander.core.subsystems.contract.AttackSubsystem;
import com.binary_studio.fleet_commander.core.subsystems.contract.DefenciveSubsystem;
import com.binary_studio.fleet_commander.core.subsystems.contract.Subsystem;

public final class DockedShip implements ModularVessel {

	private final String name;

	private final PositiveInteger shieldHP;

	private final PositiveInteger hullHP;

	private final PositiveInteger powergridOutput;

	private final PositiveInteger capacitorAmount;

	private final PositiveInteger capacitorRechargeRate;

	private final PositiveInteger speed;

	private final PositiveInteger size;

	private AttackSubsystem attackSubsystem;

	private DefenciveSubsystem defenciveSubsystem;

	private DockedShip(String name, PositiveInteger shieldHP, PositiveInteger hullHP, PositiveInteger powergridOutput,
			PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate, PositiveInteger speed,
			PositiveInteger size) {

		this.name = name;
		this.shieldHP = shieldHP;
		this.hullHP = hullHP;
		this.powergridOutput = powergridOutput;
		this.capacitorAmount = capacitorAmount;
		this.capacitorRechargeRate = capacitorRechargeRate;
		this.speed = speed;
		this.size = size;
	}

	public static DockedShip construct(String name, PositiveInteger shieldHP, PositiveInteger hullHP,
			PositiveInteger powergridOutput, PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate,
			PositiveInteger speed, PositiveInteger size) {

		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Name should be not null and not empty");
		}

		return new DockedShip(name, shieldHP, hullHP, powergridOutput, capacitorAmount, capacitorRechargeRate, speed,
				size);
	}

	@Override
	public void fitAttackSubsystem(AttackSubsystem subsystem) throws InsufficientPowergridException {

		if (subsystem != null) {
			this.throwErrorIfIsNotEnoughPowerFor(subsystem);
		}

		this.attackSubsystem = subsystem;

	}

	@Override
	public void fitDefensiveSubsystem(DefenciveSubsystem subsystem) throws InsufficientPowergridException {

		if (subsystem != null) {
			this.throwErrorIfIsNotEnoughPowerFor(subsystem);
		}

		this.defenciveSubsystem = subsystem;
	}

	public CombatReadyShip undock() throws NotAllSubsystemsFitted {

		if (this.attackSubsystem == null && this.defenciveSubsystem == null) {
			throw NotAllSubsystemsFitted.bothMissing();
		}
		else if (this.attackSubsystem == null) {
			throw NotAllSubsystemsFitted.attackMissing();
		}
		else if (this.defenciveSubsystem == null) {
			throw NotAllSubsystemsFitted.defenciveMissing();
		}

		return new CombatReadyShip(this);
	}

	private void throwErrorIfIsNotEnoughPowerFor(Subsystem subsystem) throws InsufficientPowergridException {
		int remainingEnergy = this.powergridOutput.value() - subsystem.getPowerGridConsumption().value();

		if (this.attackSubsystem != null) {
			remainingEnergy -= this.attackSubsystem.getPowerGridConsumption().value();
		}

		if (this.defenciveSubsystem != null) {
			remainingEnergy -= this.defenciveSubsystem.getPowerGridConsumption().value();
		}

		if (remainingEnergy < 0) {
			throw new InsufficientPowergridException(-remainingEnergy);
		}
	}

	public String getName() {
		return this.name;
	}

	public PositiveInteger getSpeed() {
		return this.speed;
	}

	public PositiveInteger getSize() {
		return this.size;
	}

	public PositiveInteger getShieldHP() {
		return this.shieldHP;
	}

	public PositiveInteger getHullHP() {
		return this.hullHP;
	}

	public PositiveInteger getCapacitorAmount() {
		return this.capacitorAmount;
	}

	public PositiveInteger getCapacitorRechargeRate() {
		return this.capacitorRechargeRate;
	}

	public AttackSubsystem getAttackSubsystem() {
		return this.attackSubsystem;
	}

	public DefenciveSubsystem getDefenciveSubsystem() {
		return this.defenciveSubsystem;
	}

}
