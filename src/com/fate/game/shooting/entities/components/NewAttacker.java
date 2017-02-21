package com.fate.game.shooting.entities.components;

import com.fate.engine.entities.Component;
import com.fate.engine.events.EventDispatcher;
import com.fate.engine.events.EventListener;
import com.fate.game.shooting.controller.Action;
import com.fate.game.shooting.controller.GenericAction;
import com.fate.game.shooting.entities.components.attacks.Attack;
import com.fate.game.shooting.events.AttackEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 1/9/2017.
 */
public class NewAttacker extends Component {
    private List<GenericAction<Attack>> attacks;

    public NewAttacker() {
        attacks = new ArrayList<>();

        EventDispatcher.INSTANCE.addEventHandler(this);
    }

    @Override
    public Component clone() {
        return new NewAttacker();
    }

    public void addAttack(String trigger, Attack attack) {
        attacks.add(new GenericAction<>(trigger, attack));
    }

//    @EventListener(AttackEvent.class)
//    public void handleAttackEvent(AttackEvent e) {
//        if (e.getSource() != parent)
//            return;
//
//        switch (e.getSelection()) {
//            case ONE:
//                att1.execute(e);
//                break;
//            case TWO:
//                att2.execute(e);
//                break;
//            case THREE:
//                att3.execute(e);
//                break;
//            case FOUR:
//                att4.execute(e);
//                break;
//        }
//    }
}
