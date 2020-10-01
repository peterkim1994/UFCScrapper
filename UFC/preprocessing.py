import pandas as pd
import numpy as np
import tensorflow
import keras
from tensorflow.keras.optimizers import Adam, Adagrad, SGD
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Activation, Dense, Dropout
from sklearn.utils import shuffle

x = pd.read_csv('fightdata.csv')
print(x.head)

print(x)

head2headLabel= x['FIGHTER1WIN'].values
methodLabel = x['FIGHTER1OUTCOME'].values
print(x.size)
x.drop(['FIGHTER1WIN','FIGHTER1OUTCOME','COUNTRY', 'COUNTRY2','COUNTRYOFEVENT','EVENTDATE','DOB','DOB2','FIGHTER1','FIGHTER2','FIGHTERa','FIGHTERb','WEIGHT2','HEIGHT','HEIGHT2'], axis=1, inplace=True)
x.drop(['FIGHTER1RECENT3','FIGHTER1RECENT4','REACH','REACH2','FIGHTER2RECENT32','FIGHTER2RECENT42'], axis=1, inplace=True)
print(x.size)
dataSet = x.values.astype('float32')

trainingSet,trainingLabels = shuffle(dataSet,head2headLabel)

#1,len(dataSet[1])
model = Sequential([
    Dense( units = 900, input_dim=(trainingSet.shape[1]), activation = 'relu',
       #      activity_regularizer = keras.regularizers.l2(0.01)
           ),
    Dropout( rate=0.4),
    Dense(units = 60, activation = 'relu' ,
    #      activity_regularizer = keras.regularizers.l2(0.01)
                ),
    Dense(units = 20, activation = 'relu'),
    Dense(units =1 , activation= 'sigmoid')
])
print(model.summary())
model.compile(optimizer=Adam(learning_rate= 0.0001), loss ='binary_crossentropy', metrics =['accuracy'])#preparing the model for training

#binary_crossentropy

model.fit(x = trainingSet, y = trainingLabels, validation_split=0.05, batch_size=22, epochs=50, shuffle = False, verbose=2)
#cm = confusion_matrix(y_true=testNumericalLabels,y_pred = rounded_predictions)