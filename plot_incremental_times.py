import plotly.plotly as py
import plotly.graph_objs as go

xValues = []
yValues = []
maxY = 0

filename = 'CD_incremental'

with open(filename + '.txt', 'r') as inFile:
	for line in inFile:
		xValues.append(int(line.split(',')[0]))
		y = int(line.split(',')[1])/pow(10,6) 
		if y > maxY:
			maxY = y
		yValues.append(y)

print("maxY: " + str(maxY))

# Create a trace
trace = go.Scatter(
    x = xValues,
    y = yValues,
    name = 'incdupdet_plot_incremental_times_for_'
)

layout = go.Layout(
    title='Time per getDuplicates() depending on the number of inserted records',
    xaxis=dict(
        title='number of inserted records',
        titlefont=dict(
            family='Courier New, monospace',
            size=18,
            color='#7f7f7f'
        )
    ),
    yaxis=dict(
        title='time in milliseconds',
        titlefont=dict(
            family='Courier New, monospace',
            size=18,
            color='#7f7f7f'
        )
    )
)

data = [trace]

fig = go.Figure(data=data, layout=layout)

plot_url = py.plot(fig, filename='incdupdet_plot_incremental_times_for_')