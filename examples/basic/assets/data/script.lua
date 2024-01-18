local ShapeRenderer = java.import("com.badlogic.gdx.graphics.glutils.ShapeRenderer")
local GL20 = java.import("com.badlogic.gdx.graphics.GL20")
local Gdx = java.import("com.badlogic.gdx.Gdx")

local renderer = ShapeRenderer.new()

local moveState = 1;
-- 1 = RIGHT
-- 2 = TOP
-- 3 = LEFT
-- 4 = BOTTOM

local recX = 50.0;
local recY = 50.0;
local recSize = 100.0
local recSpeed = 5.0
local halfRec = recSize / 2.0

function render()
    Gdx.gl.glClearColor(1.0, 1.0, 1.0, 1.0)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    moveBox()

    renderer:Begin(ShapeRenderer.ShapeType.Filled)
    renderer:setColor(1.0, 0.0, 0.0 ,1.0)
    renderer:rect(recX - halfRec, recY - halfRec, recSize, recSize)
    renderer:End()
end

function moveBox()
    local width = Gdx.graphics.getWidth()
    local height = Gdx.graphics.getHeight()

    local borderLimitRight = width - halfRec;
    local borderLimitLeft = halfRec;
    local borderLimitTop = height - halfRec;
    local borderLimitBottom = halfRec;

    if moveState == 1 then
        if recX < borderLimitRight then
            recX = recX + recSpeed
        else
            moveState = 2
        end
    elseif moveState == 2 then
        if recY < borderLimitTop then
            recY = recY + recSpeed
        else
            moveState = 3
        end
    elseif moveState == 3 then
        if recX > borderLimitLeft then
            recX = recX - recSpeed
        else
            moveState = 4;
        end
    elseif moveState == 4 then
        if recY > borderLimitBottom then
            recY = recY - recSpeed;
        else
            moveState = 1;
        end
    end
end