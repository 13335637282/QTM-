import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List;

public class Pet extends JFrame {
    // 颜文字集合
    private static final String[] KAOMOJI = {
            "(｡･ω･｡)", "(≧∇≦)ﾉ", "ヽ(✿ﾟ▽ﾟ)ノ", "(●'◡'●)", "(*^▽^*)",
            "(￣ω￣;)", "(￣ε￣＠)", "(￣▽￣*)ゞ", "(◕‿◕✿)", "(づ｡◕‿‿◕｡)づ",
            "ヾ(◍°∇°◍)ﾉﾞ", "٩(◕‿◕｡)۶", "(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧", "ヽ(○´∀`)ﾉ♪", "(ノ°ο°)ノ",
            "ヾ(●ω●)ノ", "(ﾉ´ヮ`)ﾉ*: ･ﾟ", "(●ˊωˋ●)", "(｡♥‿♥｡)", "(◠‿◠✿)"
    };

    // 宠物状态枚举
    public enum PetState {
        IDLE, HAPPY, SAD, SLEEPING, EATING, PLAYING, WALKING
    }

    // 宠物属性
    private String name;
    private int hunger;
    private int happiness;
    private int energy;
    private PetState state;
    private String currentKaomoji;
    private Color backgroundColor;

    // UI组件
    private JLabel petLabel;
    private JPanel controlPanel;
    private JButton feedButton;
    private JButton playButton;
    private JButton sleepButton;
    private JButton cleanButton;
    private JButton customizeButton;

    // 拖拽相关
    private int mouseX, mouseY;

    // 定时器
    private Timer statTimer;
    private Timer animationTimer;
    private Timer walkTimer;

    // 散步相关
    private Point targetPosition;
    private boolean isWalking = false;
    private Random random = new Random();

    // 交互系统
    private List<PetInteractionListener> interactionListeners = new ArrayList<>();

    public Pet(String name) {
        this.name = name;
        this.hunger = 50;
        this.happiness = 70;
        this.energy = 80;
        this.state = PetState.IDLE;
        this.backgroundColor = new Color(240, 248, 255, 200); // 半透明浅蓝色背景

        initializeUI();
        setupTimers();
        setupDragListener();
        setupWalkBehavior();
    }

    private void initializeUI() {
        setTitle(name + " - 桌面宠物");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // 完全透明背景
        setAlwaysOnTop(true); // 确保窗口置顶

        // 宠物显示区域
        petLabel = new JLabel(getRandomKaomoji(), JLabel.CENTER);
        petLabel.setFont(new Font("微软雅黑", Font.PLAIN, 40));
        petLabel.setOpaque(true);
        petLabel.setBackground(backgroundColor);
        updatePetAppearance();

        // 控制面板
        controlPanel = new JPanel(new FlowLayout());
        controlPanel.setOpaque(false);

        feedButton = new JButton("喂食");
        playButton = new JButton("玩耍");
        sleepButton = new JButton("睡觉");
        cleanButton = new JButton("清洁");
        customizeButton = new JButton("自定义");

        // 按钮样式
        styleButton(feedButton);
        styleButton(playButton);
        styleButton(sleepButton);
        styleButton(cleanButton);
        styleButton(customizeButton);

        // 添加按钮事件
        feedButton.addActionListener(e -> feed());
        playButton.addActionListener(e -> play());
        sleepButton.addActionListener(e -> sleep());
        cleanButton.addActionListener(e -> clean());
        customizeButton.addActionListener(e -> showCustomizationDialog());

        // 添加组件
        controlPanel.add(feedButton);
        controlPanel.add(playButton);
        controlPanel.add(sleepButton);
        controlPanel.add(cleanButton);
        controlPanel.add(customizeButton);

        setLayout(new BorderLayout());
        add(petLabel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupWalkBehavior() {
        walkTimer = new Timer();
        walkTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    // 只有空闲状态且不处于拖动状态时才散步
                    if (getPetState() == PetState.IDLE && !isWalking && energy > 30) {
                        startRandomWalk();
                    }
                });
            }
        }, 10000, 10000); // 每10秒检查一次是否开始散步
    }

    private void startRandomWalk() {
        if (isWalking) return;

        isWalking = true;
        setPetState(PetState.WALKING);

        // 获取屏幕尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // 随机生成目标位置（确保在屏幕内）
        int targetX = random.nextInt(screenSize.width - getWidth());
        int targetY = random.nextInt(screenSize.height - getHeight());
        targetPosition = new Point(targetX, targetY);

        // 散步动画定时器
        Timer walkAnimationTimer = new Timer();
        walkAnimationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    Point currentPos = getLocation();
                    int dx = targetPosition.x - currentPos.x;
                    int dy = targetPosition.y - currentPos.y;

                    // 计算步长（根据距离调整速度）
                    int stepX = dx == 0 ? 0 : (dx > 0 ? 2 : -2);
                    int stepY = dy == 0 ? 0 : (dy > 0 ? 2 : -2);

                    // 更新位置
                    setLocation(currentPos.x + stepX, currentPos.y + stepY);

                    // 更新颜文字方向
                    if (stepX > 0) {
                        currentKaomoji = "→" + currentKaomoji.replaceAll("→|←", "");
                    } else if (stepX < 0) {
                        currentKaomoji = "←" + currentKaomoji.replaceAll("→|←", "");
                    }
                    petLabel.setText(currentKaomoji);

                    // 检查是否到达目标位置
                    if (Math.abs(dx) < 5 && Math.abs(dy) < 5) {
                        walkAnimationTimer.cancel();
                        isWalking = false;
                        setPetState(PetState.IDLE);

                        // 消耗能量
                        energy = Math.max(0, energy - 10);

                        // 有30%几率继续走到新位置
                        if (random.nextDouble() < 0.3 && energy > 20) {
                            startRandomWalk();
                        }
                    }
                });
            }
        }, 0, 50); // 每50毫秒更新一次位置
    }

    private void styleButton(JButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });
    }

    private void setupTimers() {
        // 状态定时器 - 每10秒更新一次状态
        statTimer = new Timer();
        statTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> updateStats());
            }
        }, 10000, 10000);

        // 动画定时器 - 每3秒更换一次颜文字
        animationTimer = new Timer();
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (getPetState() == PetState.IDLE && !isWalking) {
                        animatePet();
                    }
                });
            }
        }, 3000, 3000);
    }

    private void setupDragListener() {
        // 添加鼠标监听器到整个窗口
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();

                // 点击交互
                if (e.getClickCount() == 2) {
                    petClicked();
                }

                // 如果正在散步，停止散步
                if (isWalking) {
                    isWalking = false;
                    setPetState(PetState.IDLE);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int newX = getX() + e.getX() - mouseX;
                int newY = getY() + e.getY() - mouseY;
                setLocation(newX, newY);
            }
        });

        // 确保标签也能触发拖动
        petLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getXOnScreen() - getX();
                mouseY = e.getYOnScreen() - getY();

                // 如果正在散步，停止散步
                if (isWalking) {
                    isWalking = false;
                    setPetState(PetState.IDLE);
                }
            }
        });

        petLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
            }
        });
    }

    // 宠物功能方法
    public void feed() {
        if (state == PetState.SLEEPING || isWalking) return;

        hunger = Math.min(100, hunger + 20);
        energy = Math.max(0, energy - 5);
        setPetState(PetState.EATING);
        fireInteractionEvent("feed");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> setPetState(PetState.IDLE));
            }
        }, 2000);
    }

    public void play() {
        if (state == PetState.SLEEPING || energy < 20 || isWalking) return;

        happiness = Math.min(100, happiness + 15);
        hunger = Math.max(0, hunger - 10);
        energy = Math.max(0, energy - 15);
        setPetState(PetState.PLAYING);
        fireInteractionEvent("play");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> setPetState(PetState.IDLE));
            }
        }, 3000);
    }

    public void sleep() {
        if (isWalking) return;

        if (state == PetState.SLEEPING) {
            setPetState(PetState.IDLE);
            fireInteractionEvent("wakeup");
        } else {
            setPetState(PetState.SLEEPING);
            fireInteractionEvent("sleep");
        }
    }

    public void clean() {
        if (isWalking) return;

        backgroundColor = new Color(240, 248, 255, 200); // 重置背景色
        petLabel.setBackground(backgroundColor);
        happiness = Math.min(100, happiness + 10);
        fireInteractionEvent("clean");
    }

    public void petClicked() {
        if (isWalking) return;

        happiness = Math.min(100, happiness + 5);
        setPetState(PetState.HAPPY);
        fireInteractionEvent("pet");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> setPetState(PetState.IDLE));
            }
        }, 1500);
    }

    // 状态更新方法
    private void updateStats() {
        if (getPetState() != PetState.SLEEPING) {
            hunger = Math.max(0, hunger - 5);
            energy = Math.max(0, energy - 3);
            happiness = Math.max(0, happiness - 2);
        } else {
            // 睡觉时恢复能量
            energy = Math.min(100, energy + 15);
        }

        // 根据状态更新颜文字
        updatePetAppearance();

        // 检查是否需要通知
        if (hunger < 20) {
            fireInteractionEvent("hungry");
        }
        if (happiness < 20) {
            fireInteractionEvent("sad");
        }
        if (energy < 10 && getPetState() != PetState.SLEEPING) {
            fireInteractionEvent("tired");
        }
    }

    private void animatePet() {
        if (getPetState() == PetState.IDLE && !isWalking) {
            currentKaomoji = getRandomKaomoji();
            petLabel.setText(currentKaomoji);
        }
    }

    private void updatePetAppearance() {
        // 根据状态和属性更新外观
        switch (getPetState()) {
            case HAPPY:
                currentKaomoji = "(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧";
                break;
            case SAD:
                currentKaomoji = "(╥﹏╥)";
                break;
            case SLEEPING:
                currentKaomoji = "(∪｡∪)｡｡｡zzz";
                break;
            case EATING:
                currentKaomoji = "(っ˘ڡ˘ς)";
                break;
            case PLAYING:
                currentKaomoji = "ヾ(◍°∇°◍)ﾉﾞ";
                break;
            case WALKING:
                currentKaomoji = "→(｡･ω･｡)"; // 默认向右走
                break;
            default:
                if (hunger < 20) {
                    currentKaomoji = "(´• ω •`)ﾉ";
                } else if (happiness < 20) {
                    currentKaomoji = "(︶︹︺)";
                } else if (energy < 20) {
                    currentKaomoji = "(￣o￣) zzZZ";
                } else {
                    currentKaomoji = getRandomKaomoji();
                }
        }

        // 更新颜色
        if (happiness > 70) {
            petLabel.setForeground(new Color(255, 105, 180)); // 粉红色
        } else if (happiness < 30) {
            petLabel.setForeground(Color.GRAY);
        } else {
            petLabel.setForeground(Color.BLACK);
        }

        petLabel.setText(currentKaomoji);
    }

    // 辅助方法
    private String getRandomKaomoji() {
        return KAOMOJI[random.nextInt(KAOMOJI.length)];
    }

    private void showCustomizationDialog() {
        if (isWalking) return;

        JDialog dialog = new JDialog(this, "自定义宠物", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(300, 200);

        JLabel nameLabel = new JLabel("宠物名字:");
        JTextField nameField = new JTextField(name);

        JLabel colorLabel = new JLabel("背景颜色:");
        JButton colorButton = new JButton("选择");
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(dialog, "选择背景颜色", backgroundColor);
            if (newColor != null) {
                backgroundColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), 200);
                petLabel.setBackground(backgroundColor);
            }
        });

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            name = nameField.getText();
            setTitle(name + " - 桌面宠物");
            dialog.dispose();
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(colorLabel);
        dialog.add(colorButton);
        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // 状态设置方法
    public void setPetState(PetState newState) {
        this.state = newState;
        updatePetAppearance();
    }

    public PetState getPetState() {
        return state;
    }

    // 属性获取方法
    public int getHunger() {
        return hunger;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getEnergy() {
        return energy;
    }

    public String getCurrentKaomoji() {
        return currentKaomoji;
    }

    // 交互事件系统
    public interface PetInteractionListener {
        void onPetInteraction(String interactionType);
    }

    public void addInteractionListener(PetInteractionListener listener) {
        interactionListeners.add(listener);
    }

    public void removeInteractionListener(PetInteractionListener listener) {
        interactionListeners.remove(listener);
    }

    private void fireInteractionEvent(String interactionType) {
        for (PetInteractionListener listener : interactionListeners) {
            listener.onPetInteraction(interactionType);
        }
    }

    // 主方法
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Pet pet = new Pet("小颜");

            // 添加交互监听器示例
            pet.addInteractionListener(interactionType -> {
                System.out.println("宠物交互: " + interactionType);
            });
        });
    }
}