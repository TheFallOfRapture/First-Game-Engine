package com.fate.game.shooting.entities.components;

import com.fate.engine.entities.Component;
import com.fate.engine.events.EventDispatcher;
import com.fate.engine.events.EventListener;
import com.fate.game.shooting.controller.Action;
import com.fate.game.shooting.entities.components.attacks.Attack;
import com.fate.game.shooting.events.AttackEvent;

import java.util.ArrayList;

/**
 * Created by Fernando on 1/9/2017.
 */
public class Attacker extends Component {
    private Attack att1, att2, att3, att4;

    public Attacker(Attack att1, Attack att2, Attack att3, Attack att4) {
        this.att1 = att1;
        this.att2 = att2;
        this.att3 = att3;
        this.att4 = att4;

        EventDispatcher.INSTANCE.addEventHandler(this);
    }
    
    @Override
    public Component clone() {
        return new Attacker(att1, att2, att3, att4);
    }

    @EventListener(AttackEvent.class)
    public void handleAttackEvent(AttackEvent e) {
        if (e.getSource() != parent)
            return;

        switch (e.getSelection()) {
            case ONE:
                att1.execute(e);
                break;
            case TWO:
                att2.execute(e);
                break;
            case THREE:
                att3.execute(e);
                break;
            case FOUR:
                att4.execute(e);
                break;
        }
    }
}
