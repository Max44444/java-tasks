package com.binary_studio.fleet_commander.core.ship;

import java.util.Optional;

import com.binary_studio.fleet_commander.core.actions.attack.AttackAction;
import com.binary_studio.fleet_commander.core.actions.defence.AttackResult;
import com.binary_studio.fleet_commander.core.actions.defence.RegenerateAction;
import com.binary_studio.fleet_commander.core.common.Attackable;
import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.ship.contract.CombatReadyVessel;

public final class CombatReadyShip implements CombatReadyVessel {

	private final DockedShip ship;

	private PositiveInteger currentShieldHP;

	private PositiveInteger currentHullHP;

	private PositiveInteger currentCapacitorAmount;

	public CombatReadyShip(DockedShip ship) {
		this.currentShieldHP = ship.getShieldHP();
		this.currentHullHP = ship.getHullHP();
		this.currentCapacitorAmount = ship.getCapacitorAmount();
		this.ship = ship;
	}

	@Override
	public void endTurn() {
		var rechargedCapacitorAmount = PositiveInteger.sum(this.ship.getCapacitorRechargeRate(),
				this.currentCapacitorAmount);
		this.currentCapacitorAmount = PositiveInteger.min(rechargedCapacitorAmount, this.ship.getCapacitorAmount());
	}

	@Override
	public void startTurn() {
	}

	@Override
	public String getName() {
		return this.ship.getName();
	}

	@Override
	public PositiveInteger getSize() {
		return this.ship.getSize();
	}

	@Override
	public PositiveInteger getCurrentSpeed() {
		return this.ship.getSpeed();
	}

	@Override
	public Optional<AttackAction> attack(Attackable target) {
		var consumption = this.ship.getAttackSubsystem().getCapacitorConsumption();

		if (this.currentCapacitorAmount.compareTo(consumption) < 0) {
			return Optional.empty();
		}
		else {
			this.currentCapacitorAmount = PositiveInteger.sub(this.currentCapacitorAmount, consumption);
			var damage = this.ship.getAttackSubsystem().attack(target);
			var attackAction = new AttackAction(damage, this, target, this.ship.getAttackSubsystem());
			return Optional.of(attackAction);
		}
	}

	@Override
	public AttackResult applyAttack(AttackAction attack) {

		var damage = this.ship.getDefenciveSubsystem().reduceDamage(attack).damage;
		var shieldDamage = PositiveInteger.min(this.currentShieldHP, damage);
		var hullDamage = PositiveInteger.sub(damage, shieldDamage);

		if (this.currentHullHP.compareTo(hullDamage) <= 0) {
			return new AttackResult.Destroyed();
		}

		this.currentHullHP = PositiveInteger.sub(this.currentHullHP, hullDamage);
		this.currentShieldHP = PositiveInteger.sub(this.currentShieldHP, shieldDamage);

		return new AttackResult.DamageRecived(attack.weapon, damage, attack.target);
	}

	@Override
	public Optional<RegenerateAction> regenerate() {

		var consumption = this.ship.getDefenciveSubsystem().getCapacitorConsumption();

		if (this.currentCapacitorAmount.compareTo(consumption) < 0) {
			return Optional.empty();
		}

		this.currentCapacitorAmount = PositiveInteger.sub(this.currentCapacitorAmount, consumption);

		var regenerateAction = this.ship.getDefenciveSubsystem().regenerate();
		var hullRegen = regenerateHullHP(regenerateAction);
		var shieldRegen = regenerateShieldHP(regenerateAction);

		return Optional.of(new RegenerateAction(shieldRegen, hullRegen));
	}

	private PositiveInteger regenerateHullHP(RegenerateAction regenerateAction) {
		var hullRegen = regenerateHP(this.currentHullHP, regenerateAction.hullHPRegenerated, this.ship.getHullHP());
		this.currentHullHP = PositiveInteger.sum(this.currentHullHP, hullRegen);
		return hullRegen;
	}

	private PositiveInteger regenerateShieldHP(RegenerateAction regenerateAction) {
		var shieldRegen = regenerateHP(this.currentShieldHP, regenerateAction.shieldHPRegenerated,
				this.ship.getShieldHP());
		this.currentShieldHP = PositiveInteger.sum(this.currentShieldHP, shieldRegen);
		return shieldRegen;
	}

	private PositiveInteger regenerateHP(PositiveInteger currentValue, PositiveInteger availableHP,
			PositiveInteger maxHP) {
		var missingHP = PositiveInteger.sub(maxHP, currentValue);
		return PositiveInteger.min(missingHP, availableHP);
	}

}
