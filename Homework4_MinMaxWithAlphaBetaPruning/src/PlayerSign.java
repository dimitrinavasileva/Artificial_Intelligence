public enum PlayerSign {
    X_PLAYER('X'),
    O_PLAYER('O'),
    EMPTY('_');

    private final Character sign;

    PlayerSign(Character sign) {
        this.sign = sign;
    }

    public Character getPlayerSign() {
        return sign;
    }
}